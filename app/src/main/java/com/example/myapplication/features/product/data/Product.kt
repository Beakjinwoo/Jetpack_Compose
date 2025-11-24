package com.example.myapplication.features.product.data

import com.example.myapplication.features.restaurant.data.Restaurant

data class Product(
    val id: String,
    val restaurant: Restaurant,
    val name: String,
    val imgUrl: String,
    val detail: String,
    val price: Int
)
