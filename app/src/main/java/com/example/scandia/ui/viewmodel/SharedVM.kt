package com.example.scandia.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.scandia.Event
import com.example.scandia.data.local.database.entity.DoctorEntity
import com.example.scandia.data.local.database.entity.PatientEntity
import com.example.scandia.data.local.datastore.Session
import com.example.scandia.data.network.ApiConfig
import com.example.scandia.data.network.PredictResponse
import com.example.scandia.data.repository.ScandiaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SharedVM(
    private val repository: ScandiaRepository,
    private val session: Session
) : ViewModel() {

    private val _doctorExistsStatus = MutableLiveData<Boolean>()
    val doctorExistsStatus: LiveData<Boolean> = _doctorExistsStatus

    private val _doctorLoggedIn = MutableLiveData<DoctorEntity?>()
    val doctorLoggedIn: LiveData<DoctorEntity?> = _doctorLoggedIn

    private val _patient = MutableLiveData<List<PatientEntity>>()
    val patient: LiveData<List<PatientEntity>> = _patient

    private val _response = MutableLiveData<PredictResponse>()
    val response: LiveData<PredictResponse> = _response

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Event<String>>()
    val isError: LiveData<Event<String>> = _isError

    fun registerDoctor(doctorEntity: DoctorEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.registerDoctor(doctorEntity)
    }

    fun isDoctorExist(email: String) = viewModelScope.launch(Dispatchers.IO) {
        _doctorExistsStatus.postValue(repository.isDoctorExisting(email))
    }

    fun getCurrentDoctorInfo(email: String, password: String) =
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.getCurrentDoctorInfo(email, password)
            _doctorLoggedIn.postValue(data)
            if (data != null) {
                session.setSession(data)
            }
        }

    val getSession = session.getSession.asLiveData(Dispatchers.IO)

    fun clearSession() = viewModelScope.launch(Dispatchers.IO) {
        session.clearSession()
    }

    fun predict(file: MultipartBody.Part) {
        _isLoading.postValue(true)
        repository.getApiService().predict(file).enqueue(object : Callback<PredictResponse> {
            override fun onResponse(
                call: Call<PredictResponse>,
                response: Response<PredictResponse>
            ) {
                _isLoading.value = false
                when (response.code()) {
                    201 -> {
                        if (response.isSuccessful) {
                            _response.postValue(response.body())
                        }
                    }

                    else -> {
                        _isError.postValue(Event("Gambar tidak sesuai, Silahkan upload ulang"))
                    }
                }
            }

            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                _isLoading.postValue(false)
                _isError.postValue(Event(t.message.toString()))
            }
        })
    }

    fun addPatient(
        name: String,
        age: String,
        date: String,
        desc: String,
        degree: String,
    ) = viewModelScope.launch(Dispatchers.IO) {
        repository.addPatient(
            PatientEntity(
                name = name,
                age = age,
                date = date,
                desc = desc,
                degree = degree,
                doctorId = session.getSession.firstOrNull()?.id ?: 0,
            )
        )
    }

    fun getPatient() = viewModelScope.launch(Dispatchers.IO) {
        _patient.postValue(repository.getPatient(session.getSession.firstOrNull()?.id ?: 0))
    }
}

