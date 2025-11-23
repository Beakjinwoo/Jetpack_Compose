package com.example.myapplication.instance

import android.util.Log
import com.example.myapplication.api.LoginApi
import com.example.myapplication.api.ProductApi
import com.example.myapplication.api.RestaurantApi
import com.example.myapplication.data.login.LoginData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
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

    // Refresh Token을 위한 클라이언트
    private val refreshClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    // Refresh Token을 위한 retrofit
    private val refreshRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(refreshClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val refreshLoginApi by lazy {
        refreshRetrofit.create(LoginApi::class.java)
    }

    // 401 발생 시 토큰 갱신
    private val tokenAuthenticator = Authenticator { route: Route?, response: Response ->
        if (!::loginData.isInitialized) {
            Log.e("ApiInstance", "loginData 초기화 안됨 - 재시도 불가")
            return@Authenticator null
        }

        // 이미 재시도한 경우 중단
        if (response.request.header("Tokenㅇ-Refreshed") != null) {
            Log.e("ApiInstance", "이미 재시도했지만 실패 - 중단")
            return@Authenticator null
        }

        Log.d("ApiInstance", "401 발생 - 토큰 갱신 시도")

        val newAccessToken = runBlocking {
            try {
                val refreshToken = loginData.refreshToken.first()
                if (refreshToken == null) {
                    Log.e("ApiInstance", "Refresh token이 없음")
                    return@runBlocking null
                }

                val refreshAuth = "Bearer $refreshToken"
                val refreshResponse = refreshLoginApi.refreshTokenRequest(refreshAuth)

                if (refreshResponse.isSuccessful) {
                    val newToken = refreshResponse.body()?.accessToken
                    if (newToken != null) {
                        Log.d("ApiInstance", "토큰 갱신 성공")
                        // 새 토큰 저장
                        loginData.saveToken(newToken, refreshToken)
                        newToken
                    } else {
                        Log.e("ApiInstance", "새 토큰이 null")
                        null
                    }
                } else {
                    Log.e("ApiInstance", "토큰 갱신 실패: ${refreshResponse.code()}")
                    null
                }
            } catch (e: Exception) {
                Log.e("ApiInstance", "토큰 갱신 중 예외 발생: ${e.message}", e)
                null
            }
        }

        if (newAccessToken != null) {
            response.request.newBuilder()
                .header("Authorization", "Bearer $newAccessToken")
                .header("Token-Refreshed", "true") // 재시도 표시
                .build()
        } else {
            null
        }
    }

    private val client by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->

                val token = if (::loginData.isInitialized) {
                    runBlocking {
                        loginData.accessToken.first()
                    }
                } else {
                    Log.d("ApiInstance", "loginData 초기화 안됨 - 토큰 없이 요청")
                    null
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
            .authenticator(tokenAuthenticator)
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
