package com.example.myapplication.features.main.state

import com.example.myapplication.features.main.data.MainResponse

sealed class MainUiState {
    object Initial : MainUiState()
    data class Success(val mainResponse: MainResponse) : MainUiState()
    data class Error(val message: String) : MainUiState()
}