package com.example.scandia.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("doctor")
data class DoctorEntity(
    @PrimaryKey(true)
    val id: Int = 0,
    val name: String,
    val specialist: String,
    val email: String,
    val password: String,
)
