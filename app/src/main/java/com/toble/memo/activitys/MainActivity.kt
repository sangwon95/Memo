package com.toble.memo.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import com.toble.memo.adpter.MemoAdapter
import com.toble.memo.databinding.ActivityMainBinding
import com.toble.memo.model.MemoListEventListener
import com.toble.memo.room.MemoEntity
import com.toble.memo.utils.MemoListHelper

class MainActivity : AppCompatActivity(), MemoListEventListener {

    companion object {
        const val TAG: String = "MainActivity - 로그"
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var memoAdapter: MemoAdapter

    private var memoList = mutableListOf<MemoEntity>()

    // 메모 추가 화면 호출
    private val addLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val content = result.data?.getStringExtra("content")
                Log.i(TAG, "RESULT_OK : $content")

                //memoList.add(MemoEntity(null,"title", content.toString(), "2024-06-05", "2024-06-05"))
                //  val data: Intent? = result.data
                //  val title = data?.getStringExtra("title")
                //  val content = data?.getStringExtra("content")
                //  val date = data?.getStringExtra("date")
                //  val time = data?.getStringExtra("time")
                //  memoList.add(MemoEntity(memoList.size + 1, title, content, date, time))
                //  memoAdapter.notifyDataSetChanged()

                memoAdapter.add(MemoEntity(null,"title", content.toString(), "2024-06-05", "2024-06-05"))
                memoAdapter.notifyDataSetChanged()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }
        memoList.add(MemoEntity(null, "title1", "content1", "2024-06-05", "2024-06-05"))
        initMemoAdapter()
    }

    override fun onResume() {
        super.onResume()

        // 메모 추가 버튼 클릭
        binding.addImageView.setOnClickListener {
        Intent(this, EditActivity::class.java).apply {
            addLauncher.launch(this)
        }
      }
    }


    private fun initMemoAdapter() {
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


    override fun changedMemoListListener(updatedList: MutableList<MemoEntity>) {
        TODO("Not yet implemented")
    }
}