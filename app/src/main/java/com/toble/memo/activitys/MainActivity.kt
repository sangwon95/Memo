package com.toble.memo.activitys

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import com.toble.memo.adpter.MemoAdapter
import com.toble.memo.databinding.ActivityMainBinding
import com.toble.memo.model.MemoData
import com.toble.memo.model.MemoItemClickListener
import com.toble.memo.repository.MemoRepository
import com.toble.memo.room.MemoDatabase
import com.toble.memo.room.MemoEntity
import com.toble.memo.utils.FormatDate
import com.toble.memo.utils.MemoListHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), MemoItemClickListener {

    companion object {
        const val TAG: String = "로그 - MainActivity:"
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var memoAdapter: MemoAdapter

    private lateinit var memoDB: MemoDatabase //Room Database

    private var memoList = mutableListOf<MemoEntity>()

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
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getParcelableExtra("memoData", MemoData::class.java)
                } else {
                    @Suppress("DEPRECATION") // 사용되지 않는 정보
                    result.data?.getParcelableExtra("memoData") as? MemoData
                }?.let { memoData ->
                    Log.d(TAG, "memoData: $memoData")
                    val today = FormatDate.todayFormatDate()
                    val memoEntity = MemoEntity(memoData.id, memoData.content, memoData.position, memoData.createdAt, today)

                    MemoRepository.updateEdit(memoData.id!!, memoData.content, today)
                    memoAdapter.edit(memoData.position, memoEntity)
                } ?: Log.i(TAG, "RESULT_CANCEL!!")


//                result.data?.getStringExtra("content")?.let {
//                    Log.i(TAG, "RESULT_OK : $it")
//                    result.data?.getIntExtra("position", -1)?.let { position ->
//                        val today = FormatDate.todayFormatDate()
//                        val memoEntity = MemoEntity(null, it, today, today)
//
//                        MemoRepository.updateEdit(memoEntity)
//                        memoAdapter.edit(position, MemoEntity(null, it, "2024-06-05", today))
//                    }
//                }
            } else {
                Log.i(TAG, "RESULT_CANCEL")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }
        initMemoDataBase()
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
    }

    private fun initMemoDataBase() {
        memoDB = MemoDatabase.getInstance(this)!! //Room Database 초기화
        MemoRepository.initialize(memoDB)

        // Room DB에 저장된 모든 메모 가져오기
        lifecycleScope.launch {
            MemoRepository.getAll().let {
                memoList.addAll(it)
                initMemoAdapter(memoList)
            }
        }
    }

    private fun initMemoAdapter(memoList: MutableList<MemoEntity>) {
        memoAdapter = MemoAdapter(memoList, this)
        binding.memoRecyclerView.adapter = memoAdapter

        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.memoRecyclerView.addItemDecoration(dividerItemDecoration)

        val callback = MemoListHelper(memoAdapter) { removePosition ->
            Log.i(TAG, "removePosition: $removePosition")
        }

        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.memoRecyclerView)
    }
    
    override fun memoItemClickEvent(content: String, position: Int) {
        Intent(this, EditActivity::class.java).apply {
            Log.d(TAG, "memoItemClickEvent: ${memoList[position]}")
            Log.d(TAG, "memoItemClickEvent: ${memoList[position].id}")

            val memoData = MemoData(
                memoList[position].id,
                "EDIT",
                position,
                content,
                memoList[position].createdAt,
                memoList[position].updateAt
            )

            this.putExtra("memoData", memoData)
            editLauncher.launch(this)
        }
    }
}