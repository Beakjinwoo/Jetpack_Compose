package com.example.myapplication.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.myapplication.data.LoginData
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
            LaunchedEffect(Unit) {
                val isLoggedIn = loginData.isLoggedIn.first()
                if (isLoggedIn) {
                    val token = loginData.accessToken.first()
                    if (token != null) {
                        moveToMain(token)
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                UsernameInput(loginViewModel.userName) { loginViewModel.updateUsername(it) }
                Spacer(modifier = Modifier.height(16.dp))

                PasswordInput(loginViewModel.passWord) { loginViewModel.updatePassword(it)}
                Spacer(modifier = Modifier.height(24.dp))

                SubmitButton()
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = loginViewModel.result)
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
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
    }

    @Composable
    fun SubmitButton() {
        Button(
            onClick = {
                lifecycleScope.launch {
                    val success = loginViewModel.login()
                    if (success) {
                        val token = loginData.accessToken.first()
                        if (token != null) {
                            moveToMain(token)
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("로그인", fontSize = 16.sp)
        }
    }
}