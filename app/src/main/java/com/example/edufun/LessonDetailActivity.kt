package com.example.edufun

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edufun.adapter.ChapterAdapter
import com.example.edufun.database.ChapterDatabaseHelper
import com.example.edufun.databinding.ActivityLessonDetailBinding

class LessonDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLessonDetailBinding
    private lateinit var chapterDatabaseHelper: ChapterDatabaseHelper
    private lateinit var chapterAdapter: ChapterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityLessonDetailBinding.inflate(layoutInflater)
            setContentView(binding.root)
            Log.d("LessonDetailActivity", "Binding initialized successfully")

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

            chapterDatabaseHelper = ChapterDatabaseHelper(this)
            chapterDatabaseHelper.addChapter("Bab 1", "Bilangan Bulat dan Macam Operasi Bilangan Bulat", "https://youtu.be/aB0XoMjEYuM?si=EdfXNQ9CRwE3-nYa")
            Log.d("LessonDetailActivity", "ChapterDatabaseHelper initialized successfully")

            val lessonTitle = intent.getStringExtra("lesson_title") ?: "Mata Pelajaran"
            val lessonDesc = intent.getStringExtra("lesson_desc") ?: "Deskripsi Mata Pelajaran"
            val imageResId = intent.getIntExtra("image_res_id", 0)

            binding.tvTitle.text = lessonTitle
            binding.tvDesc.text = lessonDesc

            if (imageResId != 0) {
                binding.ivBackground.setImageResource(imageResId)
            } else {
                Log.w("LessonDetailActivity", "Image resource ID is not provided or is 0")
            }

            setupRecyclerView()
        } catch (e: Exception) {
            Log.e("LessonDetailActivity", "Error in onCreate", e)
            finish()
        }
    }

    private fun setupRecyclerView() {
        try {
            val chapters = chapterDatabaseHelper.getAllChapters()
            Log.d("LessonDetailActivity", "Chapters retrieved: ${chapters.size}")

            chapterAdapter = ChapterAdapter(chapters, this)
            binding.rvChapter.layoutManager = LinearLayoutManager(this)
            binding.rvChapter.adapter = chapterAdapter
        } catch (e: Exception) {
            Log.e("LessonDetailActivity", "Error setting up RecyclerView", e)
        }
    }
}
