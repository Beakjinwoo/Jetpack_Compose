package com.example.myapplication.features.restaurant.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.core.network.ApiInstance
import com.example.myapplication.features.restaurant.data.Restaurant
import com.example.myapplication.features.restaurant.state.RestaurantApiState
import kotlinx.coroutines.launch

class RestaurantViewModel : ViewModel() {
    var after by mutableStateOf("")
    var count by mutableIntStateOf(10)
        private set
    var productCount by mutableIntStateOf(0)
        private set

    val preList = mutableListOf<Restaurant>()

    var fetchMore by mutableStateOf(false)
        private set

    var apiState by mutableStateOf<RestaurantApiState>(RestaurantApiState.Initial)
        private set

    fun loadContent() {

        // 중복 방지
        if (fetchMore) {
            return
        }

        viewModelScope.launch {
            fetchMore = true

            try {
                Log.d("RestaurantViewModel", "API 호출 ")
                val response = ApiInstance.restaurantApi.getRestaurants(after, count)

                if (response.isSuccessful) {
                    val restaurantResponse = response.body()


                    if (restaurantResponse != null) {
                        val newRestaurants = restaurantResponse.data

                        if (newRestaurants.isNotEmpty()) {
                            preList.addAll(newRestaurants)
                            productCount += newRestaurants.size
                            after = newRestaurants.last().id
                            apiState = RestaurantApiState.Success(preList.toList())
                            Log.d("RestaurantViewModel", "데이터 추가 성공")
                        } else {
                            Log.d("RestaurantViewModel", "추가할 데이터가 없음")
                        }
                    } else {
                        Log.d("RestaurantViewModel", "응답 데이터 없음")
                        apiState = RestaurantApiState.Error("응답 데이터가 없음")
                    }
                } else {
                    Log.d("RestaurantViewModel", "API 응답 실패: ${response.code()}")
                    apiState = RestaurantApiState.Error("API 응답 실패: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.d("RestaurantViewModel", "통신 오류: ${e.message}")
                apiState = RestaurantApiState.Error("통신 오류: ${e.message}")
            } finally {
                fetchMore = false
            }
        }
    }
}