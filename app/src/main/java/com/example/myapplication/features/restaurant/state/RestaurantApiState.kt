package com.example.myapplication.features.restaurant.state

import com.example.myapplication.features.restaurant.data.Restaurant

sealed class RestaurantApiState {
    object Initial : RestaurantApiState()

    object Loading : RestaurantApiState()

    data class Success(val response: List<Restaurant>) : RestaurantApiState()

    data class Error(val message: String) : RestaurantApiState()
}