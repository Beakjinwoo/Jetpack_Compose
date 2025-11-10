package com.example.myapplication.HelperInstance

import android.content.Context
import android.os.Build
import android.webkit.JavascriptInterface
import android.widget.Toast

/**
 * JavaScript에서 Kotlin 함수를 호출하기 위한 Bridge
 */
class JsToKotlinInterface(
    private val context: Context,
    private val token: String?
) {

    // Toast 메시지 띄우기
    @JavascriptInterface
    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    // 디바이스 정보 가져오기
    @JavascriptInterface
    fun getDeviceInfo(): String {
        return "Android Device - API ${Build.VERSION.SDK_INT}"
    }

    // 사용자 토큰 가져오기
    @JavascriptInterface
    fun getUserToken(): String {
        return token ?: "No token available"
    }
}