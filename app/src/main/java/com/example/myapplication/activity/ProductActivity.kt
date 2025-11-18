package com.example.myapplication.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.apiservice.ProductInstance
import com.example.myapplication.common.composable.LoadingIndicator
import com.example.myapplication.data.login.LoginData
import com.example.myapplication.data.product.Product
import com.example.myapplication.state.ProductApiState
import com.example.myapplication.viewmodel.ProductViewModel
import kotlinx.coroutines.flow.distinctUntilChanged

class ProductActivity: ComponentActivity() {
    private val productViewModel: ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val loginData = LoginData(this)

        ProductInstance.initialize(loginData = loginData)

        // 초기 데이터 로드
        productViewModel.loadContent()

        setContent {
            MainScreen()
        }
    }

    @Composable
    fun MainScreen() {
        when (val state = productViewModel.apiState) {
            is ProductApiState.Initial -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("데이터 로딩중...")
                }
            }

            is ProductApiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingIndicator()
                }
            }

            is ProductApiState.Success -> {
                ProductList(products = state.response)
            }

            is ProductApiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "오류: ${state.message}", color = Color.Red)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { productViewModel.loadContent() }) {
                            Text("재시도")
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ProductList(products: List<Product>) {
        val listState = rememberLazyListState()

        // 마지막 아이템 도달 감지
        val shouldLoadMore = remember {
            derivedStateOf {
                val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()

                lastVisibleItem != null &&
                        lastVisibleItem.index >= listState.layoutInfo.totalItemsCount - 3 // 마지막 3개 아이템 전에 로드
            }
        }

        // 끝에 도달 시 loadContent
        LaunchedEffect(shouldLoadMore.value) {
            if (shouldLoadMore.value && !productViewModel.isLoading) {
                productViewModel.loadContent()
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(products.size) { index ->
                ProductCard(product = products[index], number = index + 1)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // 로딩 시
            if (productViewModel.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }

    @Composable
    fun ProductCard(product: Product, number: Int) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "${number}번 제품 : ${product.name}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "가게명: ${product.restaurant.name}",
                    fontSize = 18.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "가격: ${product.price}원",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = product.detail,
                    fontSize = 18.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${product.restaurant.ratings} (${product.restaurant.ratingsCount}개)",
                    fontSize = 18.sp,
                    color = Color.Black
                )

                Text(
                    text = "배달 시간: ${product.restaurant.deliveryTime}분 / 배달 가격 ${product.restaurant.deliveryFee}원",
                    fontSize = 18.sp,
                    color = Color.Black
                )
            }
        }
    }
}