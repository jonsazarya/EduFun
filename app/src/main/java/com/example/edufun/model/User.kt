package com.example.edufun.model

data class User(
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)
