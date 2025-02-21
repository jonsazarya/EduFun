package com.example.edufun.view.chapter

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.edufun.database.ChapterDatabaseHelper
import com.example.edufun.databinding.ActivityChapterDetailBinding

class ChapterDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChapterDetailBinding
    private lateinit var chapterDatabaseHelper: ChapterDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChapterDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        chapterDatabaseHelper = ChapterDatabaseHelper(this)

        val chapterTitle = intent.getStringExtra("chapter_title") ?: "Bab pelajaran"
        val chapterDesc = intent.getStringExtra("chapter_desc") ?: "Deskripsi Bab"

        binding.tvChapterTitle.text = chapterTitle
        binding.tvChapterDesc.text = chapterDesc

        setupView()
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
}