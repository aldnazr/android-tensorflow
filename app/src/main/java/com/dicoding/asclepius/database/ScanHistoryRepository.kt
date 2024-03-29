package com.dicoding.asclepius.database

import androidx.lifecycle.LiveData
import com.dicoding.asclepius.database.dao.ScanHistoryDao
import com.dicoding.asclepius.database.entity.ScanHistory

class ScanHistoryRepository(private val scanHistoryDao: ScanHistoryDao) {
    fun insertData(scanHistory: ScanHistory) {
        scanHistoryDao.insertData(scanHistory)
    }

    fun readData(): LiveData<List<ScanHistory>> {
        return scanHistoryDao.readData()
    }

    fun deleteData(id: Int) {
        scanHistoryDao.deleteData(id)
    }
}