package com.toble.memo.room

interface RoomListener {

    fun onInsertListener(memo: MemoEntity)

    fun onGetAllListener()
}