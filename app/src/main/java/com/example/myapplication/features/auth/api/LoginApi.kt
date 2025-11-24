package com.example.myapplication.features.auth.api

import com.example.myapplication.features.auth.data.LoginResponse
import com.example.myapplication.features.auth.data.RefreshResponse
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginApi {
    @POST("auth/login")
    suspend fun postLoginRequest(
        @Header("Authorization") authorization: String
    ): Response<LoginResponse>

    @POST("auth/token")
    suspend fun refreshTokenRequest(
        @Header("Authorization") authorization: String
    )
    : Response<RefreshResponse>
}