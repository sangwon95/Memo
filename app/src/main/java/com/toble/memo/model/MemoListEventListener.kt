package com.toble.memo.model

import com.toble.memo.room.MemoEntity

interface MemoItemClickListener {
    fun memoItemClickEvent(content: String, position: Int)
}