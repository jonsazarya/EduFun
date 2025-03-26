package com.example.edufun.view.chapter

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.MediaController
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.edufun.R
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
        val videoName = intent.getStringExtra("video_name") ?: ""

        binding.tvChapterTitle.text = chapterTitle
        binding.tvChapterDesc.text = chapterDesc

        setupVideo(videoName)
        setupView()
    }

    private fun setupVideo(videoName: String) {
        val videoView: VideoView = binding.vwChapter // Pastikan ID ini sesuai dengan layout XML Anda
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)

        // Mengatur URI video dari folder raw
        val videoUri: Uri = Uri.parse("android.resource://${packageName}/raw/$videoName")
        videoView.setVideoURI(videoUri)

        // Memulai pemutaran video
        videoView.start()

        // Menangani kesalahan saat memutar video
        videoView.setOnErrorListener { mp, what, extra ->
            // Tampilkan pesan kesalahan atau lakukan penanganan kesalahan di sini
            true // Mengembalikan true jika kesalahan ditangani
        }
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
