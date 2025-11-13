package com.example.myapplication.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.myapplication.state.ProductApiState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.myapplication.apiservice.ProductInstance
import com.example.myapplication.data.product.ProductRequest
import com.example.myapplication.data.product.ProductResponse
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    var after by mutableStateOf(null)
         private set

    var count by mutableStateOf(0)
        private set


    var apiState by mutableStateOf(ProductApiState.Initial)
        private set

    fun Initialize() {
    }

    fun LoadContent() {
        viewModelScope.launch{

            try {
                //after / count
                val response = ProductInstance.api.GetProducts()
            }
        }
    }
}