package com.example.edufun.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.edufun.model.History

@Dao
interface QuizHistoryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuiz(quiz: History)

    @Query("SELECT * FROM quiz_history")
    suspend fun getAllQuiz(): List<History>

    @Delete
    suspend fun deleteQuiz(quiz: History)
}



