package com.example.myapplication.data.login

data class LoginResponse(
    val accessToken: String?,
    val refreshToken: String? = null,
    val id: Long,
    val username: String,
    val email: String
)