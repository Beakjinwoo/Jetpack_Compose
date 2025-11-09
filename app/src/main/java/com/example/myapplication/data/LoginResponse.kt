package com.example.myapplication.data

data class LoginResponse(
    val accessToken: String?,
    val refreshToken: String? = null,
    val id: Long,
    val username: String,
    val email: String
)