package com.example.edufun.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_history")
data class History(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val score: String
)

