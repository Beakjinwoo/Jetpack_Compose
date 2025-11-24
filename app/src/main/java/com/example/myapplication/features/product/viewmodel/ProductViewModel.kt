package com.example.myapplication.features.product.viewmodel

import com.example.myapplication.core.network.ApiInstance
import com.example.myapplication.core.viewmodel.BasePaginationViewModel
import com.example.myapplication.features.product.data.Product

class ProductViewModel : BasePaginationViewModel<Product>() {

    override val tag = "ProductViewModel"

    override suspend fun fetchData(after: String, count: Int): Result<List<Product>> {
        val response = ApiInstance.productApi.getProducts(after, count)

        if (response.isSuccessful) {
            val data = response.body()?.data
            if (data != null) {
                return Result.success(data)
            } else {
                return Result.failure(Exception("응답 데이터가 없음"))
            }
        } else {
            return Result.failure(Exception("API 응답 실패: ${response.code()}"))
        }
    }

    override fun getItemId(item: Product): String = item.id
}
