package com.example.scandia.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.scandia.data.local.database.dao.DoctorDao
import com.example.scandia.data.local.database.dao.PatientDao
import com.example.scandia.data.local.database.entity.DoctorEntity
import com.example.scandia.data.local.database.entity.PatientEntity


@Database(entities = [DoctorEntity::class, PatientEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun doctorDao(): DoctorDao

    abstract fun patientDao(): PatientDao

    companion object {
        private const val DB_NAME = "scandia.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        @JvmStatic
        fun getInstance(ctx: Context): AppDatabase {
            return INSTANCE ?: synchronized(AppDatabase::class.java) {

                val instances = Room.databaseBuilder(
                    ctx.applicationContext, AppDatabase::class.java, DB_NAME
                ).build()
                instances
            }
        }
    }
}