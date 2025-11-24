package com.example.myapplication.features.webview.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.features.webview.bridge.JsToKotlinInterface
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WebViewActivity : ComponentActivity() {
    private var webView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView?.canGoBack() == true) {
                    webView?.goBack()
                } else {
                    finish()
                }
            }
        })

        setContent {
            WebViewScreen()
        }

        lifecycleScope.launch {
            delay(3000)
            callJavaScriptFunction()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Composable
    fun WebViewScreen() {
        val token = intent.getStringExtra("token")
        AndroidView(
            modifier = Modifier.Companion.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    webView = this

                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        loadWithOverviewMode = true
                        useWideViewPort = true
                    }

                    webViewClient = WebViewClient()

                    addJavascriptInterface(JsToKotlinInterface(context, token), "Android")

                    loadUrl("file:///android_asset/index.html")
                }
            }
        )
    }

    // 자바스크립트 함수인 updateUI를 코틀린에서 사용
    private fun callJavaScriptFunction(){
            webView?.evaluateJavascript(
                "updateUI('브릿지 성공')", null
            )
    }
}