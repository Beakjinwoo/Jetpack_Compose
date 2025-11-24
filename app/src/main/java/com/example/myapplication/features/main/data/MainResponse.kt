package com.example.myapplication.features.main.data

data class MainResponse(
    val user: User,
    val welcomeMessage: String,
    val todos: List<Todos>
)
