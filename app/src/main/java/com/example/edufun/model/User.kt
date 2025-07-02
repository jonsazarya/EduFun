package com.example.edufun.model

data class User(
    val id: Int,
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)
