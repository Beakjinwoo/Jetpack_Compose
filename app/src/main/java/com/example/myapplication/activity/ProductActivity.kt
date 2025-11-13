package com.example.myapplication.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.product.Restaurant

class ProductActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
        }
    }

    @Composable
    fun MainScreen(){

    }

    @Composable
    fun RestaurantList(restaurant: Restaurant){
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = restaurant.id,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = restaurant.name,
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Text(
                    text = restaurant.thumbUrl,
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Text(
                    text = restaurant.tags.toString(),
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Text(
                    text = restaurant.priceRange,
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Text(
                    text = restaurant.ratings.toString(),
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Text(
                    text = restaurant.ratings.toString(),
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Text(
                    text = restaurant.ratings.toString(),
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Text(
                    text = restaurant.ratings.toString(),
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
        }
    }
}