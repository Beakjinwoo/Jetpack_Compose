package com.example.myapplication.features.restaurant.api

import com.example.myapplication.features.restaurant.data.RestaurantResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RestaurantApi {
    @GET("/restaurant")
    suspend fun getRestaurants(
        @Query("after") after: String,
        @Query("count") count: Int
    ): Response<RestaurantResponse>
}