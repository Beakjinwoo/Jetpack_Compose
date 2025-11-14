package com.example.myapplication.data.product

data class ProductResponse<T>(
    val meta: Meta,
    val data: T
)
