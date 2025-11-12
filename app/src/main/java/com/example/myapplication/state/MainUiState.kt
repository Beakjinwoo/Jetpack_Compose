package com.example.myapplication.state

sealed class MainUiState {
    object Initial : MainUiState()
    object Refreshing : MainUiState()
    data class Success(val refreshCount: Int) : MainUiState()
    data class Error(val message: String) : MainUiState()
}