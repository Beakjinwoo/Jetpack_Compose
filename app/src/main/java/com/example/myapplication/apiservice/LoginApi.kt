package com.example.myapplication.apiservice

import com.example.myapplication.data.LoginRequest
import com.example.myapplication.data.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("auth/login")
    suspend fun postLoginRequest(
        @Body request: LoginRequest
    ): Response<LoginResponse>
}