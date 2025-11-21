package com.example.myapplication.api

import com.example.myapplication.data.product.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

interface ProductApi {
    //get 요청에서 @BODY가 아니라 @Query 사용해야한다
    @GET("/product")
    suspend fun getProducts(
        @Query("after") after: String,
        @Query("count") count: Int
    ): Response<ProductResponse>
}