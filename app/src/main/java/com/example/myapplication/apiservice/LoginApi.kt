package com.example.myapplication.apiservice

import com.example.myapplication.data.login.LoginRequest
import com.example.myapplication.data.login.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginApi {
    @POST("auth/login")
    suspend fun postLoginRequest(
        @Header("Authorization") authorization: String
    ): Response<LoginResponse>
}