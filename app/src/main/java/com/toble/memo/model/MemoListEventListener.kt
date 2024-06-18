package com.toble.memo.model

import com.toble.memo.room.MemoEntity

interface MemoItemClickListener {
    fun memoItemClickEvent(memo: MemoEntity)
}