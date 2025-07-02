package com.example.edufun.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.edufun.database.EdufunDatabaseHelper
import com.example.edufun.model.History
import com.example.edufun.model.QuizDetail
import com.example.edufun.pref.UserPreference
import com.example.edufun.pref.dataStore
import com.example.edufun.repo.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val edufunDatabaseHelper = EdufunDatabaseHelper(application)

    private val userRepository: UserRepository by lazy {
        val preference = UserPreference.getInstance(application.applicationContext.dataStore)
        UserRepository.getInstance(preference)
    }


    fun saveQuizToDatabase(title: String, score: String, quizId: Int) {
        viewModelScope.launch {
            val user = userRepository.getSession().first()
            val userId = user.id
            edufunDatabaseHelper.insertQuizHistory(
                title = title,
                score = score,
                userId = userId,
                quizId = quizId
            )
        }
    }

    fun getRandomQuizzesByQuizId(quizId: Int, limit: Int = 5): LiveData<List<QuizDetail>> {
        return edufunDatabaseHelper.getRandomQuizDetailsByQuizId(quizId, limit)
    }
}