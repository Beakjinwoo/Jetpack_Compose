package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.apiservice.RetrofitInstance
import com.example.myapplication.data.LoginRequest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var username by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var result by remember { mutableStateOf("") }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                UsernameInput(username) { username = it }
                Spacer(modifier = Modifier.height(16.dp))
                PasswordInput(password) { password = it }
                Spacer(modifier = Modifier.height(24.dp))
                SubmitButton(username, password) { result = it }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = result)
            }
        }
    }

    @Composable
    fun UsernameInput(value: String, onValueChange: (String) -> Unit) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text("Username 입력해주세요") },
            modifier = Modifier.fillMaxWidth()
        )
    }

    @Composable
    fun PasswordInput(value: String, onValueChange: (String) -> Unit) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text("비밀번호") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
    }

    @Composable
    fun SubmitButton(username: String, password: String, onResult: (String) -> Unit) {
        Button(
            onClick = {
                Log.d("LOGIN", "Api 통신 시작")

                lifecycleScope.launch {
                    try {
                        val response = RetrofitInstance.api.postLoginRequest(
                            LoginRequest(username, password)
                        )

                        Log.d("LOGIN", "요청 정보 : $username, $password")
                        Log.d("LOGIN", "응답 코드: ${response.code()}")
                        Log.d("LOGIN", "응답 본문: ${response.body()}")
                        Log.d("LOGIN", "에러 본문: ${response.errorBody()?.string()}")

                        if (response.isSuccessful) {
                            val token = response.body()?.accessToken
                            Log.d("LOGIN", "로그인 성공")
                            onResult("RestApi 통신 성공\n토큰: $token")
                        } else {
                            Log.d("LOGIN", "로그인 실패 : ${response.code()}")
                            onResult("실패: ${response.code()}")
                        }
                    } catch (e: Exception) {
                        Log.e("LOGIN", "예외 발생: ${e.message}", e)
                        onResult("에러: ${e.message}")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("로그인", fontSize = 16.sp)
        }
    }
}