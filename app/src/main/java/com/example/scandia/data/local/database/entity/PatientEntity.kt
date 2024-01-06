package com.example.scandia.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("patient")
data class PatientEntity(
    @PrimaryKey(true)
    val id: Int = 0,
    val name: String,
    val age: String,
    val date: String,
    val desc: String,
    val degree: String,
    val doctorId: Int,
)
