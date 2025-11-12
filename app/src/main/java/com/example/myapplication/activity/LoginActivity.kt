package com.example.myapplication.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.state.ApiResponse
import com.example.myapplication.data.login.LoginData
import com.example.myapplication.viewmodel.LoginViewModel
import kotlinx.coroutines.flow.first

class LoginActivity : ComponentActivity() {
    private lateinit var loginData: LoginData
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginData = LoginData(this)
        loginViewModel.setLoginData(loginData)

        setContent {
            //초기 로그인 데이터 확인
            LaunchedEffect(Unit) {
                val isLoggedIn = loginData.isLoggedIn.first()
                if (isLoggedIn) {
                    val token = loginData.accessToken.first()
                    if (token != null) {
                        moveToMain(token)
                    }
                }
            }

            // API state 확인 후 UI전환 + movetomain을 이관
            LaunchedEffect(loginViewModel.apiState) {
                when (val state = loginViewModel.apiState) {
                    is ApiResponse.Success -> {
                        moveToMain(state.token)
                    }
                    else -> { }
                }
            }

            LoginScreen()
        }
    }

    @Composable
    fun LoginScreen() {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                UsernameInput(
                    value = loginViewModel.userName,
                    onValueChange = { loginViewModel.updateUsername(it) },
                    enabled = loginViewModel.apiState !is ApiResponse.Loading
                )
                Spacer(modifier = Modifier.height(16.dp))

                PasswordInput(
                    value = loginViewModel.passWord,
                    onValueChange = { loginViewModel.updatePassword(it) },
                    enabled = loginViewModel.apiState !is ApiResponse.Loading

                )
                Spacer(modifier = Modifier.height(24.dp))

                SubmitButton(enabled = loginViewModel.apiState !is ApiResponse.Loading)
                Spacer(modifier = Modifier.height(16.dp))


                // Sealed class의 state로 UI변환
                when (val state = loginViewModel.apiState) {
                    is ApiResponse.Loading -> LoadingIndicator()
                    is ApiResponse.Error -> ErrorMessage(state.message)
                    is ApiResponse.Success -> SuccessMessage()
                }
            }
        }
    }

    private fun moveToMain(token: String) {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.putExtra("token", token)
        startActivity(intent)
        finish()
    }

    @Composable
    fun UsernameInput(value: String, onValueChange: (String) -> Unit, enabled: Boolean) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text("아이디를 입력해주세요") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled
        )
    }

    @Composable
    fun PasswordInput(value: String, onValueChange: (String) -> Unit, enabled: Boolean) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text("비밀번호") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled
        )
    }

    @Composable
    fun SubmitButton(enabled: Boolean) {
        Button(
            onClick = { loginViewModel.login() },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled
        ) {
            Text("로그인", fontSize = 16.sp)
        }
    }

    @Composable
    fun LoadingIndicator() {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    @Composable
    fun ErrorMessage(message: String) {
        Text(
            text = message,
            color = Color.Red,
            fontSize = 14.sp
        )
    }

    @Composable
    fun SuccessMessage() {
        Text(
            text = "로그인 성공",
            color = Color.Black,
            fontSize = 14.sp
        )
    }
}