package com.toble.memo.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update

@Dao
interface MemoDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(memo: MemoEntity): Long

    @Query("SELECT * FROM memo ORDER BY position ASC")
    fun getAll() : MutableList<MemoEntity>

    @Query("SELECT * FROM memo WHERE id = :id LIMIT 1")
    fun getMemoById(id: Long): MemoEntity

    @Query("DELETE FROM memo WHERE id = :id")
    fun delete(id: Long)

    @Query("DELETE FROM memo")
    fun deleteAll()

    @Update
    fun update(memo: MemoEntity)

    @Query("UPDATE memo SET content = :content, updateAt = :updateAt WHERE id = :id")
    fun updateEdit(id: Long, content: String, updateAt: String)

    @Query("UPDATE memo SET position = :position WHERE id = :id")
    fun updatePosition(id: Long, position: Int)
}