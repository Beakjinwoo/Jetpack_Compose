package com.example.myapplication.state

import com.example.myapplication.data.product.Product

sealed class ProductApiState {

    object Initial : ProductApiState()

    object Loading : ProductApiState()

    data class Success(val response: List<Product>) : ProductApiState()

    data class Error(val message: String) : ProductApiState()

}