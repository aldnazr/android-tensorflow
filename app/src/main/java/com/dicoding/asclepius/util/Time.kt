package com.dicoding.asclepius.util

import java.text.SimpleDateFormat
import java.util.Locale

object Time {
    fun getSecond(): String {
        val time = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return dateFormat.format(time)
    }
}