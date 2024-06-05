package com.toble.memo.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "memo")
data class MemoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,

    val title: String,

    val content: String,

    val createdAt: String,

    val updateAt: String,
)