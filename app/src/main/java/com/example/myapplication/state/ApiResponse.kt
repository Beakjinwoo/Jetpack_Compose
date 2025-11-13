package com.example.myapplication.state

sealed class ApiResponse {
    object Initial : ApiResponse()
    data class Success(val token: String) : ApiResponse()
    data class Error(val message: String) : ApiResponse()
    object Loading : ApiResponse()
}