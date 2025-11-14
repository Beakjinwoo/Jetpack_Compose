package com.example.myapplication.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.myapplication.state.ProductApiState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.myapplication.apiservice.ProductInstance
import com.example.myapplication.data.product.Product
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    // 마지막으로 로드한 id
    var after by mutableStateOf("")
    //고정 10

    var count by mutableIntStateOf(10)
        private set

    //누적 리스트
    val preProducts = mutableListOf<Product>()

    fun updateAfter(after: String) {
        this.after = after
    }


    var apiState by mutableStateOf<ProductApiState>(ProductApiState.Initial)
        private set
    fun loadContent() {
        viewModelScope.launch {

            try {
                val response = ProductInstance.api.GetProducts(after, count)

                if (response.isSuccessful) {
                    val productResponse = response.body()

                    if (productResponse != null) {
                        val products = productResponse.data
                        preProducts.addAll(products)

                        // 마지막 상품의 id를 after로 지정
                        updateAfter(products.last().id)

                        apiState = ProductApiState.Success(preProducts.toList())
                    } else {
                        apiState = ProductApiState.Error("응답 데이터가 없음")
                    }
                } else {
                    apiState = ProductApiState.Error("API 응답 실패: ${response.code()}")
                }
            } catch (e: Exception) {
                apiState = ProductApiState.Error("통신 오류: ${e.message}")
            }
        }
    }
}