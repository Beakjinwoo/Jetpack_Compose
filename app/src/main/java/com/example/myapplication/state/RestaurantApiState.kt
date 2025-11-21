package com.example.myapplication.state

import com.example.myapplication.data.restaurant.Restaurant

sealed class RestaurantApiState {
    object Initial : RestaurantApiState()

    object Loading : RestaurantApiState()

    data class Success(val response: List<Restaurant>) : RestaurantApiState()

    data class Error(val message: String) : RestaurantApiState()
}