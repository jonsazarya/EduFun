package com.example.edufun.model

data class QuizDetail(
    val id: Int,
    val quizId: Int,
    val question: String,
    val options: String,
    val answer: String
)
