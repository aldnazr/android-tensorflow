package com.dicoding.asclepius.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.database.ScanHistoryRepository
import com.dicoding.asclepius.database.entity.ScanHistory
import com.dicoding.asclepius.database.local.ScanHistoryDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryModel(application: Application) : AndroidViewModel(application) {

    // Repository
    private val scanHistoryRepository: ScanHistoryRepository

    val scanHistory: LiveData<List<ScanHistory>>

    init {
        val userDao = ScanHistoryDatabase.getInstance(application).scanHistoryDao()
        scanHistoryRepository = ScanHistoryRepository(userDao)
        scanHistory = scanHistoryRepository.readData()
    }

    fun insert(scanHistory: ScanHistory) {
        viewModelScope.launch(Dispatchers.IO) {
            scanHistoryRepository.insertData(scanHistory)
        }
    }

    fun delete(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            scanHistoryRepository.deleteData(id)
        }
    }
}