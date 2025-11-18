package com.example.myapplication.apiservice

import android.util.Log
import com.example.myapplication.data.login.LoginData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
object ProductInstance {
    private const val BASE_URL = "http://10.0.2.2:3000/"

    private lateinit var loginData: LoginData

    fun initialize(loginData: LoginData) {
        this.loginData = loginData
        Log.d("ProductInstance", "초기화 완료")
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // client도 lazy로 변경!
    private val client by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->

                val token = runBlocking {
                    loginData.accessToken.first()
                }

                val request = if (token != null) {
                    chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                } else {
                    Log.e("ProductInstance", "토큰값 null")
                    chain.request()
                }

                chain.proceed(request)
            }
            .build()
    }

    val api: ProductApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ProductApi::class.java)
    }
}