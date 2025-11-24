package com.example.myapplication.features.product.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.core.network.ApiInstance
import com.example.myapplication.core.ui.composable.LoadingIndicator
import com.example.myapplication.features.auth.data.LoginData
import com.example.myapplication.features.product.data.Product
import com.example.myapplication.features.restaurant.data.Restaurant
import com.example.myapplication.core.state.PaginationApiState
import com.example.myapplication.features.product.viewmodel.ProductViewModel
import com.example.myapplication.features.restaurant.viewmodel.RestaurantViewModel

class ProductActivity : ComponentActivity() {
    private val productViewModel: ProductViewModel by viewModels()
    private val restaurantViewModel: RestaurantViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val loginData = LoginData(this)
        ApiInstance.initialize(loginData)

        // 초기 로드
        productViewModel.loadContent()
        restaurantViewModel.loadContent()

        setContent {
            MainScreen()
        }
    }

    @Composable
    fun MainScreen() {

        var selectedTab by remember { mutableIntStateOf(0) }
        val tabTitles = listOf("제품", "레스토랑")

        Column {

            PrimaryTabRow(
                selectedTabIndex = selectedTab,
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        //인덱스 0 -> 제품 / 1 -> 레스토랑 / seletedTab = 현재 선택한 탭
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }
            when (selectedTab) {
                0 -> ProductScreen()
                1 -> RestaurantScreen()
            }
        }
    }
    @Composable
    fun ProductScreen() {
        when (val state = productViewModel.apiState) {
            is PaginationApiState.Initial -> Text("데이터 로딩중...")
            is PaginationApiState.Loading -> LoadingIndicator()
            is PaginationApiState.Success -> ProductList(state.response)
            is PaginationApiState.Error ->  {
                Column {
                    Text("오류가 발생했습니다. ${state.message}")
                    Button(onClick = { productViewModel.loadContent() }) {
                        Text("재시도")
                    }
                }
            }
        }
    }
    @Composable
    fun RestaurantScreen() {
        when (val state = restaurantViewModel.apiState) {
            is PaginationApiState.Initial -> Text("데이터 로딩중...")
            is PaginationApiState.Loading -> LoadingIndicator()
            is PaginationApiState.Success -> RestaurantList(state.response)
            is PaginationApiState.Error ->  {
                Column {
                    Text("오류가 발생했습니다. ${state.message}")
                    Button(onClick = { restaurantViewModel.loadContent() }) {
                        Text("재시도")
                    }
                }
            }
        }
    }
    @Composable
    fun ProductList(products: List<Product>) {
        val listState = rememberLazyListState()

        val shouldLoadMore = remember {
            derivedStateOf {
                val last = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                last != null &&
                        last.index >= listState.layoutInfo.totalItemsCount - 3
            }
        }

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
                ProductCard(products[index], index + 1)
                Spacer(Modifier.height(8.dp))
            }

            if (productViewModel.isLoading) {
                item {
                    LoadingIndicator()
                }
            }
        }
    }

    @Composable
    fun ProductCard(product: Product, number: Int) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(8.dp)
                ),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("${number}번 제품 : ${product.name}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text("가게명: ${product.restaurant.name}", fontSize = 16.sp)
                Text("가격: ${product.price}원", fontSize = 16.sp)
                Spacer(Modifier.height(8.dp))
                Text(product.detail, fontSize = 16.sp)
                Spacer(Modifier.height(8.dp))
                Text("${product.restaurant.ratings} (${product.restaurant.ratingsCount}개)", fontSize = 16.sp)
                Spacer(Modifier.height(8.dp))
                Text("배달 시간: ${product.restaurant.deliveryTime}분 / 배달 ${product.restaurant.deliveryFee}원", fontSize = 16.sp)
            }
        }
    }

    @Composable
    fun RestaurantList(restaurants: List<Restaurant>) {
        val listState = rememberLazyListState()

        val shouldLoadMore = remember {
            derivedStateOf {
                val last = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                last != null &&
                        last.index >= listState.layoutInfo.totalItemsCount - 3
            }
        }

        LaunchedEffect(shouldLoadMore.value) {
            if (shouldLoadMore.value && !restaurantViewModel.isLoading) {
                restaurantViewModel.loadContent()
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(restaurants.size) { index ->
                RestaurantCard(restaurants[index], index + 1)
                Spacer(Modifier.height(8.dp))
            }

            if (restaurantViewModel.isLoading) {
                item { LoadingIndicator() }
            }
        }
    }

    @Composable
    fun RestaurantCard(restaurant: Restaurant, number: Int) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(8.dp)
                ),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("${number}번 가게 : ${restaurant.name}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text("평점: ${restaurant.ratings}", fontSize = 16.sp)
                Text("리뷰 수: ${restaurant.ratingsCount}", fontSize = 16.sp)
                Text("배달비: ${restaurant.deliveryFee}원", fontSize = 16.sp)
                Text("배달시간: ${restaurant.deliveryTime}분", fontSize = 16.sp)
            }
        }
    }
}
