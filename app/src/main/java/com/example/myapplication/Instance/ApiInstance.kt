package com.example.myapplication.Instance

import android.util.Log
import com.example.myapplication.api.LoginApi
import com.example.myapplication.api.ProductApi
import com.example.myapplication.api.RestaurantApi
import com.example.myapplication.data.login.LoginData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiInstance {
    private const val BASE_URL = "http://10.0.2.2:3000/"

    private lateinit var loginData: LoginData

    fun initialize(loginData: LoginData) {
        this.loginData = loginData
        Log.d("ApiInstance", "초기화 완료")
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

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
                    Log.e("ApiInstance", "토큰값 null")
                    chain.request()
                }

                chain.proceed(request)
            }
            .build()
    }

    // Retrofit 인스턴스는 하나만 ㅁ난든다.
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val loginApi: LoginApi by lazy {
        retrofit.create(LoginApi::class.java)
    }

    val productApi: ProductApi by lazy {
        retrofit.create(ProductApi::class.java)
    }

    val restaurantApi: RestaurantApi by lazy {
        retrofit.create(RestaurantApi::class.java)
    }
}
