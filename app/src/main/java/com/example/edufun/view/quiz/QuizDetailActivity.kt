package com.example.edufun.view.quiz

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edufun.adapter.QuizDetailAdapter
import com.example.edufun.database.EdufunDatabaseHelper
import com.example.edufun.databinding.ActivityQuizDetailBinding
import com.example.edufun.model.History
import com.example.edufun.view.history.HistoryFragment
import com.example.edufun.view.main.MainActivity
import com.example.edufun.viewmodel.QuizViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class QuizDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizDetailBinding
    private lateinit var quizViewModel: QuizViewModel
    private lateinit var quizDetailAdapter: QuizDetailAdapter
    private var quizId: Int = 0
    private var correctAnswer: Int = 0
    private var inCorrectAnswer: Int = 0

    companion object {
        const val TAG = "history"
        const val TITLE = "title"
        const val RESULT_SCORE = "score"
        const val REQUEST_HISTORY_UPDATE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        quizViewModel = ViewModelProvider(this).get(QuizViewModel::class.java)

        quizId = intent.getIntExtra("quiz_id", 0)
        val quizDesc = intent.getStringExtra("quiz_desc") ?: "Deskripsi Kuis"

        binding.tvQuizDescription.text = quizDesc

        setupRecyclerView()
        setupView()

        binding.btnSubmit.setOnClickListener {
            val score = correctAnswer * 20
            val title =  binding.tvQuizDescription.text
            val quizId = intent.getIntExtra("quiz_id", -1)

            if (score != null) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, "Cek skor kamu di riwayat", Toast.LENGTH_SHORT).show()
                quizViewModel.saveQuizToDatabase(
                    title.toString(),
                    score.toString(),
                    quizId
                )
            } else {
                finish()
            }
        }
    }

    private fun setupRecyclerView() {
        binding.progressBar.visibility = View.VISIBLE
        binding.rvQuiz.visibility = View.GONE
        binding.tvNoQuizzes.visibility = View.GONE
        binding.btnSubmit.visibility = View.GONE

        Handler(Looper.getMainLooper()).postDelayed({
            // Ganti pemanggilan ke fungsi random
            quizViewModel.getRandomQuizzesByQuizId(quizId).observe(this) { quizzes ->
                binding.progressBar.visibility = View.GONE

                if (quizzes.isEmpty()) {
                    binding.rvQuiz.visibility = View.GONE
                    binding.btnSubmit.visibility = View.GONE
                    binding.tvNoQuizzes.visibility = View.VISIBLE
                } else {
                    quizDetailAdapter = QuizDetailAdapter(quizzes, this) { quiz, selectedOption ->
                        if (selectedOption == quiz.answer) {
                            correctAnswer++
                        } else {
                            inCorrectAnswer++
                        }
                    }

                    binding.rvQuiz.layoutManager = LinearLayoutManager(this)
                    binding.rvQuiz.adapter = quizDetailAdapter
                    binding.rvQuiz.visibility = View.VISIBLE
                    binding.btnSubmit.visibility = View.VISIBLE
                    binding.tvNoQuizzes.visibility = View.GONE
                }
            }
        }, 1000)
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun moveToHistory(title: String, score: String) {
        val intent = Intent(this, HistoryFragment::class.java)
        intent.putExtra(TITLE, title)
        intent.putExtra(RESULT_SCORE, score)
        setResult(RESULT_OK, intent)
        startActivity(intent)
        finish()
    }
}
