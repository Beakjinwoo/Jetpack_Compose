package com.example.myapplication.features.auth.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignupViewModel : ViewModel() {

    // 단계 구분 (1: 이메일, 2: 휴대폰 인증)
    var currentStep by mutableStateOf(1)
        private set

    // 1 단계: 이메일/비밀번호
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var passwordConfirm by mutableStateOf("") // 비밀번호 확인

    // Step 2: 휴대폰 인증
    var phoneNumber by mutableStateOf("")
        private set
    var verificationCode by mutableStateOf("")
        private set
    var isCodeSent by mutableStateOf(false)      // 인증 번호 발송 여부
        private set
    var isVerified by mutableStateOf(false)       // 인증 완료 여부
        private set
    var timerSeconds by mutableIntStateOf(180)    // 타이머
        private set

    // 에러 메시지
    var errorMessage by mutableStateOf<String?>(null)

    // 타이머
    private var timerJob: Job? = null

    // 1 단계 메서드

    // 다음 단계로 이동 함수
    fun goToStep2() {
        errorMessage = null
        if (email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
            errorMessage = "아이디와 비밀번호를 입력해주세요"
        } else if (password != passwordConfirm) {
            errorMessage = "비밀번호가 일치하지 않습니다"
        } else {
            currentStep = 2
        }
    }

    // 이전 단계로 이동 (Step 2 초기화)
    fun goToStep1() {
        currentStep = 1
        phoneNumber = ""
        verificationCode = ""
        isCodeSent = false
        isVerified = false
        timerSeconds = 180
        timerJob?.cancel()
    }

    // 2단계 함수

    // 전화번호 입력 (숫자, 11자리 제한)
    fun updatePhoneNumber(value: String) {
        val phone = value.filter { it.isDigit() }
        if (phone.length <= 11) phoneNumber = phone
    }

    // 인증번호 입력 (숫자, 6자리 제한)
    fun updateVerificationCode(value: String) {
        val code = value.filter { it.isDigit() }
        if (code.length <= 6) verificationCode = code
    }

    // 인증번호 발송 + 타이머 시작
    fun sendCode() {
        isCodeSent = true
        verificationCode = ""
        timerJob?.cancel()
        timerSeconds = 180
        timerJob = viewModelScope.launch {
            while (timerSeconds > 0) {
                delay(1000)
                timerSeconds--
            }
        }
    }

    // 인증번호 확인
    fun verifyCode() {
        errorMessage = null
        if (verificationCode == "123456") { // todo: 실제 API 연동
            timerJob?.cancel()
            isVerified = true
        } else {
            errorMessage = "인증번호가 일치하지 않습니다"
        }
    }

    // ViewModel 종료 시 타이머 종료
    override fun onCleared() {
        timerJob?.cancel()
    }
}
