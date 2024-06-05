package com.toble.memo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import com.toble.memo.adpter.MemoAdapter
import com.toble.memo.databinding.ActivityMainBinding
import com.toble.memo.model.MemoListEventListener
import com.toble.memo.room.MemoEntity
import com.toble.memo.utils.MemoListHelper
import java.util.Date

class MainActivity : AppCompatActivity(), MemoListEventListener {

    companion object {
        const val TAG: String = "MainActivity - 로그"
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var memoAdapter: MemoAdapter

    private var memoList = mutableListOf<MemoEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }
        memoList.add(MemoEntity(1, "title1", "content1", "2024-06-05", "2024-06-05"))
        initMemoAdapter()
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