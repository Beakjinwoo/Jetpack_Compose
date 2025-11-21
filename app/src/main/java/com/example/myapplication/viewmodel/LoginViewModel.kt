package com.example.myapplication.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.apiservice.LoginInstance
import com.example.myapplication.state.ApiResponse
import com.example.myapplication.data.login.LoginData
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    var userName by mutableStateOf("")
        private set
    var passWord by mutableStateOf("")
        private set
    var apiState by mutableStateOf<ApiResponse>(ApiResponse.Initial)
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

    fun login() {
        if (userName.isEmpty() || passWord.isEmpty()) {
            apiState = ApiResponse.Error("아이디와 비밀번호를 입력해주세요")
            return
        }

        viewModelScope.launch {
            apiState = ApiResponse.Loading
            Log.d("LOGIN", "Api 통신 시작")

            try {
                val credentials = "$userName:$passWord"
                val basicAuth = "Basic " + android.util.Base64.encodeToString(
                    credentials.toByteArray(),
                    android.util.Base64.NO_WRAP
                )

                val response = LoginInstance.api.postLoginRequest(basicAuth)

                if (response.isSuccessful) {
                    val accessToken = response.body()?.accessToken
                    val refreshToken = response.body()?.refreshToken

                    if (accessToken != null && refreshToken != null) {

                        Log.d("LOGIN", "로그인 성공")
                        Log.d("LOGIN", "accessToken: $accessToken")
                        Log.d("LOGIN", "refreshToken: $refreshToken")


                        loginData.saveToken(accessToken, refreshToken)
                        apiState = ApiResponse.Success(accessToken)
                    } else {
                        apiState = ApiResponse.Error("토큰이 없습니다")
                    }
                } else {
                    Log.d("LOGIN", "로그인 실패: ${response.code()}")
                    apiState = ApiResponse.Error("로그인 실패: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("LOGIN", "예외 발생: ${e.message}", e)
                apiState = ApiResponse.Error("알 수 없는 오류가 발생했습니다. 오류 메세지 : ${e.message}")
            }
        }
    }
}