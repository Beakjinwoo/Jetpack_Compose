package com.example.myapplication.state

import com.example.myapplication.data.main.MainResponse

sealed class MainUiState {
    object Initial : MainUiState()
    data class Success(val mainResponse: MainResponse) : MainUiState()
    data class Error(val message: String) : MainUiState()
}