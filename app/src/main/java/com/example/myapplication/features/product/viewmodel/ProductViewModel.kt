package com.example.myapplication.features.product.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.myapplication.features.product.state.ProductApiState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.myapplication.core.network.ApiInstance
import com.example.myapplication.features.product.data.Product
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    // 마지막으로 로드한 id
    var after by mutableStateOf("")
    //고정 10
    var count by mutableIntStateOf(10)
        private set
    // 제품 숫자
    var productCount by mutableIntStateOf(0)
        private set

    //누적 리스트
    val preProducts = mutableListOf<Product>()

    // 로딩 중복 방지 // fetchMore (더 가져오는 상태의 로딩)
    var isLoading by mutableStateOf(false)
        private set

    var apiState by mutableStateOf<ProductApiState>(ProductApiState.Initial)
        private set

    fun loadContent() {

        // 중복 방지
        if (isLoading) {
            return
        }

        viewModelScope.launch {
            isLoading = true

            try {
                Log.d("ProductViewModel", "API 호출 ")
                val response = ApiInstance.productApi.getProducts(after, count)

                if (response.isSuccessful) {
                    val productResponse = response.body()


                    if (productResponse != null) {
                        val newProducts = productResponse.data

                        if (newProducts.isNotEmpty()) {
                            preProducts.addAll(newProducts)
                            productCount += newProducts.size
                            // 마지막 상품의 id를 after로 지정
                            after = newProducts.last().id
                            apiState = ProductApiState.Success(preProducts.toList())
                            Log.d("ProductViewModel", "데이터 추가 성공")
                        } else {
                            Log.d("ProductViewModel", "추가할 데이터가 없음")
                        }
                    } else {
                        Log.d("ProductViewModel", "응답 데이터 없음")
                        apiState = ProductApiState.Error("응답 데이터가 없음")
                    }
                } else {
                    Log.d("ProductViewModel", "API 응답 실패: ${response.code()}")
                    apiState = ProductApiState.Error("API 응답 실패: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.d("ProductViewModel", "통신 오류: ${e.message}")
                apiState = ProductApiState.Error("통신 오류: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }
}