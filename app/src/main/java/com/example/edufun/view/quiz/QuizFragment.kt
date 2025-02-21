package com.example.edufun.view.quiz

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edufun.adapter.QuizAdapter
import com.example.edufun.database.QuizDatabaseHelper
import com.example.edufun.databinding.FragmentQuizBinding
import com.example.edufun.model.Quiz

class QuizFragment : Fragment(), QuizAdapter.OnQuizClickListener {

    private lateinit var binding: FragmentQuizBinding
    private lateinit var quizDatabaseHelper: QuizDatabaseHelper
    private lateinit var quizAdapter: QuizAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        quizDatabaseHelper = QuizDatabaseHelper(requireContext())

        val categoryId = arguments?.getInt("category_id", 0) ?: 0
        Log.d("QuizFragment", "Category ID received: $categoryId")

        setupRecyclerView(categoryId)
    }

    private fun setupRecyclerView(categoryId: Int) {
        try {
            val quizzes = quizDatabaseHelper.getAllQuizzesByCategoryId(categoryId)
            Log.d("QuizFragment", "Quizzes retrieved: ${quizzes.size}")

            quizAdapter = QuizAdapter(quizzes, this)
            binding.rvQuiz.layoutManager = LinearLayoutManager(requireContext())
            binding.rvQuiz.adapter = quizAdapter
        } catch (e: Exception) {
            Log.e("QuizFragment", "Error setting up RecyclerView", e)
        }
    }

    override fun onQuizClick(quiz: Quiz) {
        val intent = Intent(requireContext(), QuizDetailActivity::class.java).apply {
            putExtra("quiz_id", quiz.id)
            putExtra("quiz_desc", quiz.desc)
            putExtra("category_id", quiz.categoryId)
        }
        startActivity(intent)
    }
}
