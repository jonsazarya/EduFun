package com.example.edufun.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.edufun.database.QuizDetailDatabaseHelper
import com.example.edufun.model.QuizDetail

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val quizDetailDatabaseHelper = QuizDetailDatabaseHelper(application)

    fun getQuizzesByQuizId(quizId: Int): LiveData<List<QuizDetail>> {
        return quizDetailDatabaseHelper.getAllQuizzesByQuizId(quizId)
    }
}
