package com.example.myapplication.apiservice

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.myapplication.data.login.LoginData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object ProductInstance {
    private const val BASE_URL = "http://10.0.2.2:3000/"

    private lateinit var loginData: LoginData

    fun initialize(loginData: LoginData) {
        this.loginData = loginData
    }


    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor ( loggingInterceptor)
        .addInterceptor { a ->

            //위험도 존재, 대체바람
            val token = runBlocking {
                loginData.accessToken.first()
            }

            val request = a.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            a.proceed(request)
        }
        .build()

    val api: ProductApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ProductApi::class.java)
    }
}