package com.example.myapplication.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.main.MainResponse
import kotlinx.coroutines.launch
import com.example.myapplication.state.MainUiState
import com.google.gson.Gson

class MainViewModel : ViewModel() {


    var mainUiState by mutableStateOf<MainUiState>(MainUiState.Initial)
        private set



    //초기화 함수
    fun loadContents(context: Context){
        viewModelScope.launch {
            try{
                val mainResponse = loadMainResponse(context)
                mainUiState = MainUiState.Success(mainResponse)
            } catch (e: Exception){
                mainUiState = MainUiState.Error(e.message ?: "json to object 실패")
            }
        }
    }

    // json to object
    fun loadMainResponse(context: Context): MainResponse {
        val jsonString = context.assets.open("MainResponse.json")
            .bufferedReader()
            .use { it.readText() }

        return Gson().fromJson(jsonString, MainResponse::class.java)
    }
}