package com.toble.memo.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import com.toble.memo.adpter.MemoAdapter
import com.toble.memo.databinding.ActivityMainBinding
import com.toble.memo.model.MemoItemClickListener
import com.toble.memo.repository.MemoRepository
import com.toble.memo.room.MemoDatabase
import com.toble.memo.room.MemoEntity
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
                    memoAdapter.add(MemoEntity(null, it, "2024-06-05", "2024-06-05"))
                }
            }
        }

    // 메모 수정 화면 호출
    private val editLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.getStringExtra("content")?.let {
                    Log.i(TAG, "RESULT_OK : $it")
                    result.data?.getIntExtra("position", -1)?.let { position ->
                        memoAdapter.edit(position, MemoEntity(null, it, "2024-06-05", "2024-06-05"))
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }
        initMemoDataBase()
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

    override fun onResume() {
        super.onResume()

        // 메모 추가 버튼 클릭
        binding.addImageView.setOnClickListener {
        Intent(this, EditActivity::class.java).apply {
            this.putExtra("memoState", "ADD")
            addLauncher.launch(this)
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

            this.putExtra("memoState", "EDIT")
            this.putExtra("content", content)
            this.putExtra("position", position)
            editLauncher.launch(this)
        }
    }
}