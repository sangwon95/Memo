package com.toble.memo.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update

@Dao
interface MemoDAO {

    @Insert(onConflict = REPLACE)
    fun insert(region: MemoEntity)

    @Query(value = "SELECT * FROM memo")
    fun getAll() : MutableList<MemoEntity>

    @Delete
    fun delete(region: MemoEntity)

    @Query("DELETE FROM memo")
    fun deleteAll()

    @Update
    fun update(memo: MemoEntity)


    @Query("UPDATE memo SET content = :content, updateAt = :updateAt WHERE id = :id")
    fun updateEdit(id: Long, content: String, updateAt: String)
}