package com.example.myapplication.core.state

sealed class PaginationApiState<out T> {
    object Initial : PaginationApiState<Nothing>()
    object Loading : PaginationApiState<Nothing>()
    data class Success<T>(val response: List<T>) : PaginationApiState<T>()
    data class Error(val message: String) : PaginationApiState<Nothing>()
}
