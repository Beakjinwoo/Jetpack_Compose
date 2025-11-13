package com.example.myapplication.apiservice

import com.example.myapplication.data.product.ProductRequest
import com.example.myapplication.data.product.ProductResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header

interface ProductApi {

    @GET("/product")
    suspend fun GetProducts(@Body request: ProductRequest)
    : List<ProductResponse>

//    @GET("/product")
//    suspend fun GetProducts(@Header ("Authorization") accessToken: String , @Body request: ProductRequest)
//            : List<ProductResponse>
}