package com.example.myapplication.data.product

data class Product(
    val id: String,
    val restaurant: Restaurant,
    val name: String,
    val imgUrl: String,
    val detail: String,
    val price: Int
)
