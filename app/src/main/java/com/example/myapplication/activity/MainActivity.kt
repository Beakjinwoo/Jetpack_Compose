package com.example.myapplication.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.common.composable.LoadingIndicator
import com.example.myapplication.data.login.LoginData
import com.example.myapplication.data.main.Todos
import com.example.myapplication.state.MainUiState
import com.example.myapplication.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var loginData: LoginData
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginData = LoginData(this)
        val token = intent.getStringExtra("token")
        mainViewModel.loadContents(this)

        setContent {
            MainScreen(token)
        }
    }

    @Composable
    fun MainScreen(token: String?) {
        val uiState = mainViewModel.mainUiState

        Box(modifier = Modifier.fillMaxSize()) {
            when (uiState) {
                is MainUiState.Initial -> {
                    LoadingIndicator()
                }
                is MainUiState.Success -> {
                    MainScreenContainer(token, uiState)

                    if (mainViewModel.isRefreshing) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ){
                            LoadingIndicator()
                        }
                    }
                }
                is MainUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "에러: ${uiState.message}",
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainScreenContainer(token: String?, uiState: MainUiState.Success) {
        PullToRefreshBox(
            isRefreshing = mainViewModel.isRefreshing,
            onRefresh = {
                mainViewModel.refresh(this@MainActivity)
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }

                item { LoadWelcomeMessage(uiState.mainResponse.welcomeMessage) }
                item { Spacer(modifier = Modifier.height(16.dp)) }
                item {
                    LoadUserData(
                        name = uiState.mainResponse.user.name,
                        email = uiState.mainResponse.user.email
                    )
                }
                item { Spacer(modifier = Modifier.height(24.dp)) }
                item {
                    Text(
                        text = "오늘의 할 일",
                        fontSize = 18.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
                items(uiState.mainResponse.todos) { todo ->
                    TodoItem(todo)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
                item { Buttons(token) }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
    @Composable
    fun Buttons(token: String?) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WebViewButton(token)
            LogoutButton()
            GoToProductActivity()
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

    @Composable
    fun LoadUserData(name: String, email: String) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "사용자 정보",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "이름: $name",
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "이메일: $email",
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
        }
    }

    @Composable
    fun LoadWelcomeMessage(message: String) {
        Text(
            text = message,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }

    @Composable
    fun TodoItem(todo: Todos) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = todo.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = todo.subtitle,
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
        }
    }

    @Composable
    fun GoToProductActivity(){
        Button(
            onClick = {
                val intent = Intent(this@MainActivity, ProductActivity::class.java)
                startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Product로 이동", fontSize = 16.sp)
        }
    }
}