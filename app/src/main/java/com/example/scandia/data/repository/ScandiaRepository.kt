package com.example.scandia.data.repository

import com.example.scandia.data.local.database.dao.DoctorDao
import com.example.scandia.data.local.database.dao.PatientDao
import com.example.scandia.data.local.database.entity.DoctorEntity
import com.example.scandia.data.local.database.entity.PatientEntity
import com.example.scandia.data.network.ApiConfig


class ScandiaRepository(
    private val doctorDao: DoctorDao,
    private val patientDao: PatientDao,
    private val apiConfig: ApiConfig
) {
    suspend fun registerDoctor(doctorEntity: DoctorEntity) =
        doctorDao.registerDoctor(doctorEntity)

    suspend fun isDoctorExisting(email: String) =
        doctorDao.isDoctorExisting(email) != null

    suspend fun getCurrentDoctorInfo(email: String, password: String) =
        doctorDao.getCurrentDoctorInfo(email, password)

    suspend fun addPatient(patientEntity: PatientEntity) =
        patientDao.addPatient(patientEntity)

    suspend fun getPatient(id: Int): List<PatientEntity> =
        patientDao.getPatient(id)

    fun getApiService() =
        apiConfig.getApiService()
}