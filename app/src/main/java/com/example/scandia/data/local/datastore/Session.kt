package com.example.scandia.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.mutablePreferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.scandia.data.local.database.entity.DoctorEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class Session(private val context: Context) {

    val getSession: Flow<DoctorEntity?> = context.dataStore.data.map { preferences ->
        DoctorEntity(
            preferences[KEY_1] ?: 0,
            preferences[KEY_2].orEmpty(),
            preferences[KEY_3].orEmpty(),
            preferences[KEY_4].orEmpty(),
            preferences[KEY_5].orEmpty(),
        )
    }


    suspend fun setSession(doctorEntity: DoctorEntity) {
        context.dataStore.edit {
            it[KEY_1] = doctorEntity.id
            it[KEY_2] = doctorEntity.name
            it[KEY_3] = doctorEntity.specialist
            it[KEY_4] = doctorEntity.email
            it[KEY_5] = doctorEntity.password
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit {
            it.clear()
        }
    }

    companion object {
        private const val DATASTORE_NAME = "session_datastore_preferences"
        private val KEY_1 = intPreferencesKey("id")
        private val KEY_2 = stringPreferencesKey("name")
        private val KEY_3 = stringPreferencesKey("specialist")
        private val KEY_4 = stringPreferencesKey("email")
        private val KEY_5 = stringPreferencesKey("password")
        private val Context.dataStore by preferencesDataStore(
            name = DATASTORE_NAME
        )
    }
}