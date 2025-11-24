package com.example.myapplication.features.auth.data

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String
)