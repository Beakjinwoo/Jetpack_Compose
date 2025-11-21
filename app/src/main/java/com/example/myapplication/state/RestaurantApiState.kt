package com.example.myapplication.state

import com.example.myapplication.data.product.Product

sealed class RestaurantApiState {
    object Initial : RestaurantApiState()

    object Loading : RestaurantApiState()

    data class Success(val response: List<Product>) : RestaurantApiState()

    data class Error(val message: String) : RestaurantApiState()
}