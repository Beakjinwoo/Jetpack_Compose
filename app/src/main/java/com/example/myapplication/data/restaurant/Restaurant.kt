package com.example.myapplication.data.restaurant

data class Restaurant(
    val id: String,
    val name: String,
    val thumbUrl: String,
    val tags: List<String>,
    val priceRange: String,
    val ratings: Double,
    val ratingsCount: Int,
    val deliveryTime: Int,
    val deliveryFee: Int
)