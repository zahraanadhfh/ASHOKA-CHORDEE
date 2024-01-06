package com.example.scandia.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.scandia.data.local.database.entity.DoctorEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface DoctorDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun registerDoctor(doctorEntity: DoctorEntity) : Long

    @Query("SELECT * FROM doctor WHERE email = :email")
    suspend fun isDoctorExisting(email: String): DoctorEntity?

    @Query("SELECT * FROM doctor WHERE email = :email AND password = :password")
    suspend fun getCurrentDoctorInfo(email: String, password: String): DoctorEntity?
}