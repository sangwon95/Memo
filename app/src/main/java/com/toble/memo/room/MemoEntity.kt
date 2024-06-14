package com.toble.memo.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "memo")
data class MemoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,

    var content: String,

    val createdAt: String,

    var updateAt: String,
): Parcelable