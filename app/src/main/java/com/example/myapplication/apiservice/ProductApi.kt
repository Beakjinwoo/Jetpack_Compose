package com.example.myapplication.apiservice

import com.example.myapplication.data.product.Product
import com.example.myapplication.data.product.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST

data class RefreshTokenRequest(val refreshToken: String)
data class TokenResponse(
    val accessToken: String,
)
interface ProductApi {
    //get 요청에서 @BODY가 아니라 @Query 사용해야한다
    @GET("/product")
    suspend fun GetProducts(
        @Query("after") after: String,
        @Query("count") count: Int
    ): Response<ProductResponse<List<Product>>>

    @POST("/auth/token")
    suspend fun refreshToken(
        @Header("Authorization") refreshToken: String
    ): Response<TokenResponse>
}