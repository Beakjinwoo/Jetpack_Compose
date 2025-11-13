package com.example.myapplication.state

import com.example.myapplication.apiservice.ProductApi
import com.example.myapplication.data.product.ProductResponse

sealed class ProductApiState {

    object Initial : ProductApiState()

    object Loading : ProductApiState()

    data class Success(val response: ProductApiState) : ProductApiState()

    data class Error(val message: String) : ProductApiState()

}