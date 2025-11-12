package com.example.myapplication.state

sealed class ApiResponse {
    data class Success(val token: String) : ApiResponse()
    data class Error(val message: String) : ApiResponse()
    object Loading : ApiResponse()
}