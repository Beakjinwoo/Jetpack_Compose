package com.example.myapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PhoneAuthViewModel : ViewModel() {

    val phoneNumber = MutableLiveData<String>()
    // 전화번호 유효성 판단
    val checkValidNumber = MutableLiveData<Boolean>()
    // 전화번호 유효 메세지
    val checkValidNumberMessage = MutableLiveData<String>()



}