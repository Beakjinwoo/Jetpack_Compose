package com.example.myapplication.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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
                modifier = Modifier.Companion
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.Companion.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "메인 화면입니다.(로그인 성공)",
                    fontSize = 28.sp,
                    modifier = Modifier.Companion.padding(bottom = 16.dp)
                )
                Button(
                    onClick = {
                        val intent = Intent(this@MainActivity, WebViewActivity::class.java)
                        intent.putExtra("token", token)
                        startActivity(intent)
                    },
                    modifier = Modifier.Companion.fillMaxWidth()
                ) {
                    Text("웹뷰 열기", fontSize = 16.sp)
                }
            }
        }
    }
}