package com.toble.memo.repository

import android.util.Log
import com.toble.memo.activitys.MainActivity
import com.toble.memo.room.MemoDatabase
import com.toble.memo.room.MemoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object MemoRepository {

    private const val TAG = "RegionRepository"

    private lateinit var memoDB: MemoDatabase

    fun initialize(database: MemoDatabase) {
        memoDB = database
    }

    fun insert(memoEntity: MemoEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                memoDB.memoDao().insert(memoEntity)
            } catch (e: Exception) {
                Log.e(MainActivity.TAG, "Error inserting memo: ${e.message}")
            }
        }
    }

    fun updateEdit(id: Long, content: String, updateAt: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                memoDB.memoDao().updateEdit(id, content, updateAt)
            } catch (e: Exception) {
                Log.e(MainActivity.TAG, "Error updating memo: ${e.message}")
            }
        }
    }

    suspend fun getAll(): MutableList<MemoEntity> {
        return withContext(Dispatchers.IO) {
            val memoList = memoDB.memoDao().getAll()
            if (memoList.isEmpty()) {
                Log.d(MainActivity.TAG, "DB에 저장된 메모가 없습니다.")
            } else {
                for (i in memoList) {
                    Log.d(MainActivity.TAG, "getAllMemo: ${i.content}")
                }
            }
            memoList
        }
    }
}