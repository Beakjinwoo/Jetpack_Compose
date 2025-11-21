package com.example.myapplication.data.restaurant

import com.example.myapplication.data.product.Meta
import com.example.myapplication.data.restaurant.Restaurant

data class RestaurantResponse(
    val meta: Meta,
    val data: List<Restaurant>

)
