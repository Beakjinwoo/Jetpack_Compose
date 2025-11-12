package com.example.myapplication.data.login

sealed class ApiResponse {
    object Initial : ApiResponse()
    data class Success(val token: String) : ApiResponse()
    data class Error(val message: String) : ApiResponse()
    object Loading : ApiResponse()
}