package com.example.myapplication.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.apiservice.RetrofitInstance
import com.example.myapplication.data.LoginData
import com.example.myapplication.data.LoginRequest

class LoginViewModel : ViewModel() {

    var userName by mutableStateOf("")
        private set
    var passWord by mutableStateOf("")
        private set
    var result by mutableStateOf("")
        private set

    private lateinit var loginData: LoginData

    fun setLoginData(data: LoginData) {
        loginData = data
    }

    fun updateUsername(newUsername: String) {
        userName = newUsername
    }

    fun updatePassword(newPassword: String) {
        passWord = newPassword
    }

    suspend fun login(): Boolean {
        if (userName.isEmpty() || passWord.isEmpty()) {
            result = "아이디와 비밀번호를 입력해주세요"
            return false
        }

        Log.d("LOGIN", "Api 통신 시작")

        try {
            val response = RetrofitInstance.api.postLoginRequest(
                LoginRequest(userName, passWord)
            )

            if (response.isSuccessful) {
                val token = response.body()?.accessToken
                if (token != null) {
                    Log.d("LOGIN", "로그인 성공")
                    loginData.saveToken(token)
                    result = "로그인 성공"
                    return true
                } else {
                    result = "토큰이 없습니다"
                    return false
                }
            } else {
                Log.d("LOGIN", "로그인 실패")
                result = "실패: ${response.code()}"
                return false
            }
        } catch (e: Exception) {
            Log.e("LOGIN", "예외 발생: ${e.message}", e)
            result = "에러: ${e.message}"
            return false
        }
    }
}