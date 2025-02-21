package com.example.edufun.view.chapter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edufun.adapter.ChapterAdapter
import com.example.edufun.database.ChapterDatabaseHelper
import com.example.edufun.databinding.FragmentChapterBinding
import com.example.edufun.model.Chapter

class ChapterFragment : Fragment(), ChapterAdapter.OnChapterClickListener {

    private lateinit var binding: FragmentChapterBinding
    private lateinit var chapterDatabaseHelper: ChapterDatabaseHelper
    private lateinit var chapterAdapter: ChapterAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChapterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chapterDatabaseHelper = ChapterDatabaseHelper(requireContext())

        val categoryId = arguments?.getInt("category_id", 0) ?: 0
        Log.d("ChapterFragment", "Category ID received: $categoryId")

        setupRecyclerView(categoryId)
    }

    private fun setupRecyclerView(categoryId: Int) {
        try {
            val chapters = chapterDatabaseHelper.getAllChaptersByCategoryId(categoryId)
            Log.d("ChapterFragment", "Chapters retrieved: ${chapters.size}")

            chapterAdapter = ChapterAdapter(chapters, this)
            binding.rvChapter.layoutManager = LinearLayoutManager(requireContext())
            binding.rvChapter.adapter = chapterAdapter
        } catch (e: Exception) {
            Log.e("ChapterFragment", "Error setting up RecyclerView", e)
        }
    }

    override fun onChapterClick(chapter: Chapter) {
        val intent = Intent(requireContext(), ChapterDetailActivity::class.java).apply {
            putExtra("chapter_title", chapter.title)
            putExtra("chapter_desc", chapter.description)
            putExtra("category_id", chapter.categoryId)
        }
        startActivity(intent)
    }
}
