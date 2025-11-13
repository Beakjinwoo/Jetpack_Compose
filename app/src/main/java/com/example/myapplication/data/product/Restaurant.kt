package com.example.myapplication.data.product

data class Restaurant(
    val id: String,
    val name: String,
    val thumbUrl: String,
    val tags: Tags,
    val priceRange: String,
    val ratings: Int,
    val ratingsCount: Int,
    val deliveryTime: Int,
    val deliveryFee: Int
)
