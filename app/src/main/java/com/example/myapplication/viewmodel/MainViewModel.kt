package com.example.myapplication.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.myapplication.state.MainUiState

class MainViewModel : ViewModel() {

    var uiState by mutableStateOf<MainUiState>(MainUiState.Initial)
        private set


    //새로고침 되는지 확인용
    private var refreshCount = 0


    fun refresh(token: String?) {
        viewModelScope.launch {
            uiState = MainUiState.Refreshing


            try {
                delay(1500)
                refreshCount++
                uiState = MainUiState.Success(refreshCount)

            } catch (e: Exception) {
                uiState = MainUiState.Error(e.message ?: "새로고침 실패")
            }
        }
    }
}