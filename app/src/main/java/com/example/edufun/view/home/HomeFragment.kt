package com.example.edufun

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edufun.adapter.CategoryAdapter
import com.example.edufun.database.CategoryDatabaseHelper
import com.example.edufun.database.ChapterDatabaseHelper
import com.example.edufun.database.QuizDatabaseHelper
import com.example.edufun.databinding.FragmentHomeBinding
import com.example.edufun.model.Category

class HomeFragment : Fragment(), CategoryAdapter.OnCategoryClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var categoryDatabaseHelper: CategoryDatabaseHelper
    private lateinit var chapterDatabaseHelper: ChapterDatabaseHelper
    private lateinit var quizDatabaseHelper: QuizDatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        categoryDatabaseHelper = CategoryDatabaseHelper(requireContext())
        chapterDatabaseHelper = ChapterDatabaseHelper(requireContext())
        quizDatabaseHelper = QuizDatabaseHelper(requireContext())

        addSampleData()
        setupRecyclerView()

        return binding.root
    }

    private fun addSampleData() {
        val mathCategoryId = categoryDatabaseHelper.addCategory("Matematika", "Matematika itu menyenangkan", R.drawable.math)
        val scienceCategoryId = categoryDatabaseHelper.addCategory("IPA", "IPA itu menyenangkan", R.drawable.science)
        val seniBudayaCategoryId = categoryDatabaseHelper.addCategory("Seni Budaya", "Seni Budaya itu menyenangkan", R.drawable.seni_budaya)

        chapterDatabaseHelper.addChapter("Bab 1", "Bilangan Bulat", mathCategoryId, "https://youtu.be/aB0XoMjEYuM?si=01nLegexXEffGk0a")
        chapterDatabaseHelper.addChapter("Bab 2", "Operasi Bilangan Bulat (Perpangkatan)", mathCategoryId, "https://youtu.be/jgvhYOluzP0?si=fB1D4DFDqk4rfnZX")
        chapterDatabaseHelper.addChapter("Bab 3", "Bilangan Pecahan", mathCategoryId, "https://youtu.be/4Zhr92kpI3M?si=QD5AW1LySvAdq-vE")

        chapterDatabaseHelper.addChapter("Bab 1", "Hakikat Ilmu Sains & Metode Ilmiah", scienceCategoryId, "https://youtu.be/x4dPya-ynyY?si=fpUqhHOr8C4F65bM")
        chapterDatabaseHelper.addChapter("Bab 2", "Zat dan Perubahannya", scienceCategoryId, "https://youtu.be/XiVCpXYwnKM?si=PTePBPFuOqdxjwEZ")
        chapterDatabaseHelper.addChapter("Bab 3", "Suhu Kalor Pemuaian", scienceCategoryId, "https://youtu.be/_S8r-k-a4NQ?si=cSBZXemlivPabcui")

        chapterDatabaseHelper.addChapter("Bab 1", "Menggambar Flora, Fauna, dan Alam Benda", seniBudayaCategoryId, "https://youtu.be/YYDBZZLMTSI?si=8rnHzqpDDWdr4pd8")
        chapterDatabaseHelper.addChapter("Bab 2", "Menggambar Ragam Hias", seniBudayaCategoryId, "https://youtu.be/GM4Ov-kAsHI?si=t6JuyemDTNZUwIUs")
        chapterDatabaseHelper.addChapter("Bab 3", "Menyanyi Dengan Satu Suara (Unisono)", seniBudayaCategoryId, "https://youtu.be/GM4Ov-kAsHI?si=t6JuyemDTNZUwIUs")

        quizDatabaseHelper.addQuiz("Kuis 1", "Bilangan Bulat", mathCategoryId)
        quizDatabaseHelper.addQuiz("Kuis 2", "Operasi Bilangan Bulat (Perpangkatan", mathCategoryId)
        quizDatabaseHelper.addQuiz("Kuis 3", "Bilangan Pecahan", mathCategoryId)

        quizDatabaseHelper.addQuiz("Kuis 1", "Hakikat Ilmu Sains & Metode Ilmiah", scienceCategoryId)
        quizDatabaseHelper.addQuiz("Kuis 2", "Zat dan Perubahannya", scienceCategoryId)
        quizDatabaseHelper.addQuiz("Kuis 3", "Suhu Kalor Pemuaian", scienceCategoryId)

        quizDatabaseHelper.addQuiz("Kuis 1", "Menggambar Flora, Fauna, dan Alam Benda", seniBudayaCategoryId)
        quizDatabaseHelper.addQuiz("Kuis 2", "Menggambar Ragam Hias", seniBudayaCategoryId)
        quizDatabaseHelper.addQuiz("Kuis 3", "Menyanyi Dengan Satu Suara (Unisono)", seniBudayaCategoryId)
    }

    private fun setupRecyclerView() {
        val categories = categoryDatabaseHelper.getAllCategories()

        val categoryAdapter = CategoryAdapter(categories, this)
        binding.rvCategory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCategory.adapter = categoryAdapter
    }

    override fun onCategoryClick(category: Category) {
        val intent = Intent(requireContext(), LessonDetailActivity::class.java)
        intent.putExtra("lesson_title", category.name)
        intent.putExtra("lesson_desc", category.desc)
        intent.putExtra("image_res_id", category.imageResId)
        intent.putExtra("category_id", category.id)
        startActivity(intent)
    }
}
