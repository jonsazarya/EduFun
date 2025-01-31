package com.example.edufun

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edufun.adapter.ChapterAdapter
import com.example.edufun.database.ChapterDatabaseHelper
import com.example.edufun.databinding.ActivityLessonDetailBinding
import com.example.edufun.model.Chapter

class LessonDetailActivity : AppCompatActivity(), ChapterAdapter.OnChapterClickListener {

    private lateinit var binding: ActivityLessonDetailBinding
    private lateinit var chapterDatabaseHelper: ChapterDatabaseHelper
    private lateinit var chapterAdapter: ChapterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLessonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chapterDatabaseHelper = ChapterDatabaseHelper(this)

        val lessonTitle = intent.getStringExtra("lesson_title") ?: "Mata Pelajaran"
        val lessonDesc = intent.getStringExtra("lesson_desc") ?: "Deskripsi Mata Pelajaran"
        val imageResId = intent.getIntExtra("image_res_id", 0)
        val categoryId = intent.getIntExtra("category_id", 0)

        binding.tvTitle.text = lessonTitle
        binding.tvDesc.text = lessonDesc

        if (imageResId != 0) {
            binding.ivBackground.setImageResource(imageResId)
        } else {
            Log.w("LessonDetailActivity", "Image resource ID is not provided or is 0")
        }

        setupRecyclerView(categoryId)
    }

    private fun setupRecyclerView(categoryId: Int) {
        try {
            val chapters = chapterDatabaseHelper.getAllChaptersByCategoryId(categoryId)
            Log.d("LessonDetailActivity", "Chapters retrieved: ${chapters.size}")

            chapterAdapter = ChapterAdapter(chapters, this)
            binding.rvChapter.layoutManager = LinearLayoutManager(this)
            binding.rvChapter.adapter = chapterAdapter
        } catch (e: Exception) {
            Log.e("LessonDetailActivity", "Error setting up RecyclerView", e)
        }
    }

    override fun onChapterClick(chapter: Chapter) {
        val youtubeLink = chapter.youtubeLink
        if (youtubeLink.isNotEmpty()) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        } else {
            Log.w("LessonDetailActivity", "YouTube link is not provided for chapter: ${chapter.title}")
        }
    }
}
