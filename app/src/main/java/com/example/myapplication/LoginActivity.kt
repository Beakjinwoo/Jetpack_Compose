package com.example.myapplication

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
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

class LoginActivity : ComponentActivity() {
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
                Text(
                    text = "LoginActivity",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

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
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text("아이디를 입력해주세요") },
            modifier = Modifier.fillMaxWidth()
        )


    }

    @Composable
    fun PasswordInput(value: String, onValueChange: (String) -> Unit) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text("비밀번호") },
            //비밀번호 감추는 설정
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

                        if (response.isSuccessful) {
                            val token = response.body()?.accessToken
                            Log.d("LOGIN", "로그인 성공")
                            onResult("로그인 성공!")

                            //MainActivity로 이동
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("token", token)
                            startActivity(intent)
                            finish()
                        } else {
                            Log.d("LOGIN", "로그인 실패")
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