package com.toble.memo.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MemoEntity::class], version = 1)
abstract class MemoDatabase : RoomDatabase() {
    abstract fun memoDao(): MemoDAO

    companion object {
        private var INSTANCE: MemoDatabase? = null

        fun getInstance(context: Context) : MemoDatabase? {
            if(INSTANCE == null) {
                synchronized(MemoDatabase::class) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    MemoDatabase::class.java, "memo.db")
                    .build()
                }
            }
            return INSTANCE
        }
    }
}