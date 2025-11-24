package com.example.myapplication.features.auth.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.features.auth.viewmodel.SignupViewModel

class SignupActivity : ComponentActivity() {
    private val signupViewModel: SignupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if (signupViewModel.currentStep == 1) Step1Screen()
            else Step2Screen()
        }
    }

    @Composable
    fun Step1Screen() {
        val focusManager = LocalFocusManager.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { focusManager.clearFocus() }
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("회원가입", fontSize = 24.sp)
            Spacer(Modifier.height(32.dp))

            OutlinedTextField(
                value = signupViewModel.email,
                onValueChange = { signupViewModel.email = it },
                label = { Text("이메일") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = signupViewModel.password,
                onValueChange = { signupViewModel.password = it },
                label = { Text("비밀번호") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = signupViewModel.passwordConfirm,
                onValueChange = { signupViewModel.passwordConfirm = it },
                label = { Text("비밀번호 확인") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(24.dp))

            Button(onClick = { signupViewModel.goToStep2() }, Modifier.fillMaxWidth()) {
                Text("다음")
            }

            signupViewModel.errorMessage?.let {
                Spacer(Modifier.height(16.dp))
                Text(it, color = Color.Red)
            }
        }
    }

    @Composable
    fun Step2Screen() {
        val focusManager = LocalFocusManager.current
        val isExpired = signupViewModel.timerSeconds == 0

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable(           // 클릭 가능
                    indication = null, // 클릭 효과 없음
                    interactionSource = remember { MutableInteractionSource() }
                ) { focusManager.clearFocus() }
                .padding(32.dp)
        ) {
            TextButton(onClick = { signupViewModel.goToStep1() }) { Text("< 뒤로") }

            Spacer(Modifier.height(16.dp))
            Text("휴대폰 인증", fontSize = 24.sp)
            Spacer(Modifier.height(32.dp))

            // 전화번호 입력
            OutlinedTextField(
                value = signupViewModel.phoneNumber,
                onValueChange = { signupViewModel.updatePhoneNumber(it) },
                label = { Text("휴대폰 번호") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                enabled = !signupViewModel.isCodeSent
            )
            Spacer(Modifier.height(16.dp))

            // 발송 버튼 or 인증번호 입력
            if (!signupViewModel.isCodeSent) {
                Button(
                    onClick = { signupViewModel.sendCode() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = signupViewModel.phoneNumber.length == 11
                ) { Text("인증번호 발송") }
            } else {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = signupViewModel.verificationCode,
                        onValueChange = { signupViewModel.updateVerificationCode(it) },
                        label = { Text("인증번호 6자리") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        enabled = !signupViewModel.isVerified && !isExpired
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        "%d:%02d".format(signupViewModel.timerSeconds / 60, signupViewModel.timerSeconds % 60),
                        color = if (isExpired) Color.Red else Color.Black,
                        fontSize = 18.sp
                    )
                }
                Spacer(Modifier.height(16.dp))

                Row(Modifier.fillMaxWidth()) {
                    OutlinedButton(onClick = { signupViewModel.sendCode() }, Modifier.weight(1f), enabled = signupViewModel.timerSeconds <= 170) {
                        Text("재발송")
                    }
                    Spacer(Modifier.width(12.dp))
                    Button(
                        onClick = { signupViewModel.verifyCode() },
                        modifier = Modifier.weight(1f),
                        enabled = signupViewModel.verificationCode.length == 6 && !signupViewModel.isVerified && !isExpired
                    ) { Text("확인") }
                }
            }

            // 메시지
            signupViewModel.errorMessage?.let {
                Spacer(Modifier.height(16.dp))
                Text(it, color = Color.Red)
            }
            if (signupViewModel.isVerified) {
                Spacer(Modifier.height(16.dp))
                Text("인증 완료!", color = Color.Black, fontSize = 18.sp)
            }

            Spacer(Modifier.weight(1f))

            Button(onClick = { finish() }, Modifier.fillMaxWidth(), enabled = signupViewModel.isVerified) {
                Text("회원가입 완료")
            }
        }
    }
}
