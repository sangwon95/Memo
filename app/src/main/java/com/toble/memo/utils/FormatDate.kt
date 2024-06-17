package com.toble.memo.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object FormatDate {

    // 현재 날짜를 yyyy-MM-dd 형식으로 반환
    fun todayFormatDate(): String {
        val nowDateTime = LocalDateTime.now()
        val pattern = "yyyy-MM-dd"
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return nowDateTime.format(formatter)
    }
}