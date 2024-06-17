package com.toble.memo.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MemoData(
    val id: Long?,
    val memoState: String,
    val position: Int,
    var content: String,
    val createdAt: String,
    var updateAt: String,
) : Parcelable