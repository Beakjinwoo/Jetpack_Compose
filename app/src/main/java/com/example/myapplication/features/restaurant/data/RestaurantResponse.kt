package com.example.myapplication.features.restaurant.data

import com.example.myapplication.features.product.data.Meta

data class RestaurantResponse(
    val meta: Meta,
    val data: List<Restaurant>

)
