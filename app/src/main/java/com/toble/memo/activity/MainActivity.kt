package com.toble.memo.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import com.toble.memo.R
import com.toble.memo.adapter.MemoAdapter
import com.toble.memo.adapter.ViewPagerAdapter
import com.toble.memo.databinding.ActivityMainBinding
import com.toble.memo.model.MemoData
import com.toble.memo.model.MemoItemClickListener
import com.toble.memo.repository.MemoRepository
import com.toble.memo.room.MemoDatabase
import com.toble.memo.room.MemoEntity
import com.toble.memo.utils.FormatDate
import com.toble.memo.utils.MemoListHelper
import com.toble.memo.utils.PreferenceHelper.defaultPrefs
import com.toble.memo.utils.PreferenceHelper.set
import kotlinx.coroutines.launch

//<a href="https://www.flaticon.com/kr/free-icons/" title="공책 아이콘">공책 아이콘 제작자: Smashicons - Flaticon</a>
class MainActivity : AppCompatActivity(), MemoItemClickListener {
    companion object {
        const val TAG: String = "로그 - MainActivity:"
    }
    private lateinit var binding: ActivityMainBinding

    private lateinit var memoAdapter: MemoAdapter

    private lateinit var memoDB: MemoDatabase //Room Database

    private lateinit var splashScreen: SplashScreen
    // 메모 추가 화면 호출
    private val addLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.getStringExtra("content")?.let {
                    Log.i(TAG, "RESULT_OK : $it")
                    val today = FormatDate.todayFormatDate()
                    val itemCount = memoAdapter.itemCount
                    val memoEntity = MemoEntity(null, it, itemCount, today, today)

                    lifecycleScope.launch {
                        MemoRepository.insert(memoEntity).let { latestMemoEntity ->
                            Log.d(TAG, "latestMemoEntity: $latestMemoEntity")
                            memoAdapter.add(latestMemoEntity)
                        }
                    }
                }
            }
        }

    // 메모 수정 화면 호출
    private val editLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getParcelableExtra("memoData", MemoData::class.java)
                } else {
                    @Suppress("DEPRECATION") // 사용되지 않는 정보
                    result.data?.getParcelableExtra("memoData") as? MemoData
                }?.let { memoData ->
                    Log.d(TAG, "memoData: $memoData")
                    val today = FormatDate.todayFormatDate()
                    val memoEntity = MemoEntity(
                        memoData.id,
                        memoData.content,
                        memoData.position,
                        memoData.createdAt,
                        today
                    )

                    MemoRepository.updateEdit(memoData.id!!, memoData.content, today)
                    memoAdapter.edit(memoData.position, memoEntity)
                } ?: Log.i(TAG, "RESULT_CANCEL!!")
            } else {
                Log.i(TAG, "RESULT_CANCEL")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        splashScreen = installSplashScreen()
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }
        initOnBoarding()
        initMemoDataBase()
        initViewPager()
    }

    override fun onResume() {
        super.onResume()
        // 메모 추가 버튼 클릭
        binding.addImageView.setOnClickListener {
            Intent(this, EditActivity::class.java).apply {
                this.putExtra("memoData", MemoData(null, "ADD", -1, "", "", ""))
                addLauncher.launch(this)
            }
        }
        // 온보딩 다음(종료) 버튼
        binding.nextButton.setOnClickListener {
            val viewPager = binding.viewPager
            val currentItem = viewPager.currentItem
            val itemCount = viewPager.adapter?.itemCount ?: 0
            if (currentItem < itemCount - 1) {
                viewPager.setCurrentItem(currentItem + 1, true)
            }

            if(currentItem == 1){
                binding.nextButton.text = "종료"
            } else {
                binding.nextButton.text = "다음"
            }

            if(currentItem == 2){
                binding.groupFragment.visibility = View.GONE
                setOnBoarding()
            }
        }

        // 온보딩 스킵 버튼
        binding.skipButton.setOnClickListener {
            binding.groupFragment.visibility = View.GONE
            setOnBoarding()
        }
    }

    private fun initOnBoarding() {
        val prefs = defaultPrefs(this)
        prefs.getBoolean("onBoarding", false).apply {
            if (this) {
                binding.groupFragment.visibility = View.GONE
            }
        }
    }

    private fun setOnBoarding() {
        val prefs = defaultPrefs(this)
        prefs["onBoarding"] = true
    }

    /**
     * ViewPager 초기화
     */
    private fun initViewPager() {
        val viewPager = binding.viewPager
        val viewPagerAdapter = ViewPagerAdapter(this)
        viewPager.adapter = viewPagerAdapter

        binding.springDotsIndicator.attachTo(viewPager)
    }

    /**
     * Room Database 초기화
     */
    private fun initMemoDataBase() {
        memoDB = MemoDatabase.getInstance(this)!! //Room Database 초기화
        MemoRepository.initialize(memoDB)

        // Room DB에 저장된 모든 메모 가져오기
        lifecycleScope.launch {
            MemoRepository.getAll().apply {
                initMemoAdapter(this)
            }
        }
    }

    /**
     * 메모 어댑터 초기화
     */
    private fun initMemoAdapter(memoList: MutableList<MemoEntity>) {
        memoAdapter = MemoAdapter(memoList, this)
        binding.memoRecyclerView.adapter = memoAdapter

        val dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider_drawable)
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(dividerDrawable!!)
        binding.memoRecyclerView.addItemDecoration(dividerItemDecoration)

        val callback = MemoListHelper(memoAdapter) { removePosition ->
            Log.i(TAG, "removePosition: $removePosition")
        }

        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.memoRecyclerView)
    }

    /**
     * 메모 아이템 클릭 이벤트
     */
    override fun memoItemClickEvent(memo: MemoEntity) {
        Intent(this, EditActivity::class.java).apply {
            Log.d(TAG, "memoItemClickEvent: $memo")

            val memoData = MemoData(
                memo.id,
                "EDIT",
                memo.position,
                memo.content,
                memo.createdAt,
                memo.updateAt
            )

            this.putExtra("memoData", memoData)
            editLauncher.launch(this)
        }
    }
}

