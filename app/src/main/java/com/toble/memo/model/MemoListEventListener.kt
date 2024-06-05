package com.toble.memo.model

import com.toble.memo.room.MemoEntity

interface MemoListEventListener {
    fun changedMemoListListener(updatedList: MutableList<MemoEntity>)
}