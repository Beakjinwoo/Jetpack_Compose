package com.example.myapplication.data.main

data class MainResponse(
    val user: User,
    val welcomeMessage: String,
    val todos: List<Todos>
)
