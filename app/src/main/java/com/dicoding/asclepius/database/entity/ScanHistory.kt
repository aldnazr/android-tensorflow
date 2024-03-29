package com.dicoding.asclepius.database.entity

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("scanHistory")
data class ScanHistory(
    @PrimaryKey(true)
    @ColumnInfo("id")
    val id: Int = 0,

    @ColumnInfo("image")
    val image: String,

    @ColumnInfo("result")
    val result: String,

    @ColumnInfo("time")
    val time: String
)