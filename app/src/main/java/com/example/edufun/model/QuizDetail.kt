package com.example.edufun.model

data class QuizDetail(
    val id: Int,
    val quizId: Int,
    val question: String,
    val option1: String,
    val option2: String,
    val option3: String,
    val option4: String,
    val answer: String
)
