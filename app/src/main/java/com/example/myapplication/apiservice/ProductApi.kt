package com.example.myapplication.apiservice

import com.example.myapplication.data.product.Product
import com.example.myapplication.data.product.ProductResponse
import com.example.myapplication.data.restaurant.RestaurantResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST

interface ProductApi {
    //get 요청에서 @BODY가 아니라 @Query 사용해야한다
    @GET("/product")
    suspend fun getProducts(
        @Query("after") after: String,
        @Query("count") count: Int
    ): Response<ProductResponse>

    @GET("/restaurant")
    suspend fun getRestaurants(
        @Query("after") after: String,
        @Query("count") count: Int
    ): Response<RestaurantResponse>
}