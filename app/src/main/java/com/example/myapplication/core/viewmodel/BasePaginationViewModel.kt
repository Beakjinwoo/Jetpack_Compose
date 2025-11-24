package com.example.myapplication.core.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.core.state.PaginationApiState
import kotlinx.coroutines.launch

abstract class BasePaginationViewModel<T> : ViewModel() {

    var after by mutableStateOf("")
        protected set

    var count by mutableIntStateOf(10)
        private set

    var itemCount by mutableIntStateOf(0)
        private set

    protected val items = mutableListOf<T>()

    var isLoading by mutableStateOf(false)
        private set

    var apiState by mutableStateOf<PaginationApiState<T>>(PaginationApiState.Initial)
        private set

    protected abstract val tag: String // 로그캣의 태그

    protected abstract suspend fun fetchData(after: String, count: Int): Result<List<T>>

    protected abstract fun getItemId(item: T): String

    fun loadContent() {
        if (isLoading) return

        viewModelScope.launch {
            isLoading = true

            try {
                Log.d(tag, "API 호출")
                val result = fetchData(after, count)

                if (result.isSuccess) {
                    val newItems = result.getOrNull()!!
                    if (newItems.isNotEmpty()) {
                        items.addAll(newItems)
                        itemCount += newItems.size
                        after = getItemId(newItems.last())
                        apiState = PaginationApiState.Success(items.toList())
                        Log.d(tag, "데이터 추가 성공")
                    } else {
                        Log.d(tag, "추가할 데이터가 없음")
                    }
                } else {
                    val e = result.exceptionOrNull()
                    Log.d(tag, "오류: ${e?.message}")
                    apiState = PaginationApiState.Error(e?.message ?: "알 수 없는 오류")
                }
            } catch (e: Exception) {
                Log.d(tag, "통신 오류: ${e.message}")
                apiState = PaginationApiState.Error("통신 오류: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }
}
