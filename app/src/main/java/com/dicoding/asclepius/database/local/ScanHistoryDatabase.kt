package com.dicoding.asclepius.database.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.asclepius.database.dao.ScanHistoryDao
import com.dicoding.asclepius.database.entity.ScanHistory

@Database([ScanHistory::class], version = 1)
abstract class ScanHistoryDatabase : RoomDatabase() {

    abstract fun scanHistoryDao(): ScanHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: ScanHistoryDatabase? = null

        fun getInstance(context: Context): ScanHistoryDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): ScanHistoryDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ScanHistoryDatabase::class.java,
                "scan_history_database"
            ).build()
        }
    }
}