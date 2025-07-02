package com.example.edufun.view.chapter

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.edufun.R
import com.example.edufun.database.EdufunDatabaseHelper
import com.example.edufun.databinding.ActivityChapterDetailBinding

class ChapterDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChapterDetailBinding
    private lateinit var edufunDatabaseHelper: EdufunDatabaseHelper
    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChapterDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        edufunDatabaseHelper = EdufunDatabaseHelper(this)

        val chapterTitle = intent.getStringExtra("chapter_title") ?: "Bab pelajaran"
        val chapterDesc = intent.getStringExtra("chapter_desc") ?: "Deskripsi Bab"
        val videoName = intent.getStringExtra("video_name") ?: ""

        setupVideo(videoName)
        setupView()
    }

    private fun setupVideo(videoName: String) {
        val playerView: PlayerView = binding.playerView

        val videoUri: Uri = Uri.parse("android.resource://$packageName/raw/$videoName")

        player = ExoPlayer.Builder(this).build().also { exoPlayer ->
            playerView.player = exoPlayer
            val mediaItem = MediaItem.fromUri(videoUri)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true
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

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    private fun releasePlayer() {
        player?.release()
        player = null
    }
}
