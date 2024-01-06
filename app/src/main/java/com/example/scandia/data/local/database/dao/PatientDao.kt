package com.example.scandia.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.scandia.data.local.database.entity.PatientEntity

@Dao
interface PatientDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPatient(patientEntity: PatientEntity)

    @Query("SELECT * FROM patient WHERE doctorId = :id ORDER BY id DESC")
    suspend fun getPatient(id: Int) : List<PatientEntity>
}