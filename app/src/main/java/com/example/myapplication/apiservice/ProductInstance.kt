package com.example.myapplication.apiservice

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import okhttp3.OkHttpClient

object ProductInstance {
    private const val BASE_URL = "http://127.0.0.1:3000/"

    private const val ACCESS_TOKEN: String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InRlc3RAY29kZWZhY3RvcnkuYWkiLCJzdWIiOiJmNTViMzJkMi00ZDY4LTRjMWUtYTNjYS1kYTlkN2QwZDkyZTUiLCJ0eXBlIjoiYWNjZXNzIiwiaWF0IjoxNzYzMDIwMDM5LCJleHAiOjE3NjMwMjAzMzl9.0f3PK9MYuKuIgap2AjD2a_SdYpNKtcWjwJ-c9Nf6w6E"


    private val client = OkHttpClient.Builder()
        .addInterceptor { a ->
            val request = a.request().newBuilder()
                .addHeader("Authorization", "Bearer $ACCESS_TOKEN")
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