package com.example.myapplication.features.product.state

import com.example.myapplication.features.product.data.Product

sealed class ProductApiState {

    object Initial : ProductApiState()

    object Loading : ProductApiState()

    data class Success(val response: List<Product>) : ProductApiState()

    data class Error(val message: String) : ProductApiState()

}