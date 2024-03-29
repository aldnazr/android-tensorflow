package com.dicoding.asclepius.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.asclepius.database.entity.ScanHistory

@Dao
interface ScanHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(scanHistory: ScanHistory)

    @Query("SELECT * FROM scanHistory ORDER BY ID DESC  ")
    fun readData(): LiveData<List<ScanHistory>>

    @Query("DELETE FROM scanHistory WHERE id = :id")
    fun deleteData(id: Int)
}