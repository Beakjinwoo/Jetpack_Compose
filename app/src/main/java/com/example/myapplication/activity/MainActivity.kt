package com.example.myapplication.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.login.LoginData
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private lateinit var loginData: LoginData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //초기화 잊지 말기
        loginData = LoginData(this)
        val token = intent.getStringExtra("token")

        setContent {
            MainScreen(token)
        }
    }

    @Composable
    fun MainScreen(token: String?) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Title()
            Spacer(modifier = Modifier.height(16.dp))
            Buttons(token)
        }
    }

    @Composable
    fun Title() {
        Text(
            text = "메인 화면입니다.(로그인 성공)",
            fontSize = 28.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }

    @Composable
    fun Buttons(token: String?) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WebViewButton(token)
            LogoutButton()
        }
    }

    @Composable
    fun WebViewButton(token: String?) {
        Button(
            onClick = {
                val intent = Intent(this@MainActivity, WebViewActivity::class.java)
                intent.putExtra("token", token)
                startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("웹뷰 열기", fontSize = 16.sp)
        }
    }

    @Composable
    fun LogoutButton() {
        Button(
            onClick = {
                lifecycleScope.launch {
                    loginData.logout()

                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("로그아웃", fontSize = 16.sp)
        }
    }
}