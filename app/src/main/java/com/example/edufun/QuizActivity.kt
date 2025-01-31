package com.example.edufun

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edufun.adapter.QuizAdapter
import com.example.edufun.database.QuizDatabaseHelper
import com.example.edufun.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private lateinit var quizDatabaseHelper: QuizDatabaseHelper
    private lateinit var quizAdapter: QuizAdapter
    private var categoryId: Int = 0
    private var score: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        quizDatabaseHelper = QuizDatabaseHelper(this)
        categoryId = intent.getIntExtra("category_id", 0)

        setupRecylerView()
    }

    private fun setupRecylerView() {
        val quizzes = quizDatabaseHelper.getQuizzesByCategoryId(categoryId)

        quizAdapter = QuizAdapter(quizzes) { quiz, s ->
            if (s == quiz.answer) {
                score++
            }
        }

        binding.rvQuiz.layoutManager = LinearLayoutManager(this)
        binding.rvQuiz.adapter = quizAdapter
    }
}