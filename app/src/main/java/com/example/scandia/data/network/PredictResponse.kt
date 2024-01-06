package com.example.scandia.data.network

data class PredictResponse(
    val masking_message: String? = null,
    val regression_message: String? = null,
    val regression_result: List<List<Double?>?>? = null
)