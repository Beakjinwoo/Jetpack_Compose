package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val token = intent.getStringExtra("token")

        setContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "메인",
                    fontSize = 28.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "로그인 성공!",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (token != null) {
                    Text(
                        text = "토큰: ${token.take(20)}...",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                }

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
        }
    }
}