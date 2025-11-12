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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.login.LoginData
import com.example.myapplication.state.MainUiState
import com.example.myapplication.viewmodel.MainViewModel
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private lateinit var loginData: LoginData
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //초기화 잊지 말기
        loginData = LoginData(this)
        val token = intent.getStringExtra("token")

        setContent {
            MainScreen(token)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainScreen(token: String?) {
        val uiState = mainViewModel.uiState
        val isRefreshing = uiState is MainUiState.Refreshing

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { mainViewModel.refresh(token) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Title()
                Spacer(modifier = Modifier.height(8.dp))

                when (val state = uiState) {
                    is MainUiState.Initial -> {
                        Text("새로고침 하기 전", fontSize = 14.sp)
                    }
                    is MainUiState.Refreshing -> {
                        Text("새로고침 중..", fontSize = 14.sp, color = Color.Blue)
                    }
                    is MainUiState.Success -> {
                        Text(
                            text = "새로고침 횟수: ${state.refreshCount}",
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }
                    is MainUiState.Error -> {
                        Text(
                            text = "에러: ${state.message}",
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Buttons(token)
            }
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