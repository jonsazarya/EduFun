package com.example.edufun.view.lesson

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.edufun.R
import com.example.edufun.adapter.ChapterAdapter
import com.example.edufun.adapter.SectionsPagerAdapter
import com.example.edufun.database.EdufunDatabaseHelper
import com.example.edufun.databinding.ActivityLessonDetailBinding
import com.example.edufun.model.Chapter
import com.example.edufun.view.chapter.ChapterDetailActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class LessonDetailActivity : AppCompatActivity(), ChapterAdapter.OnChapterClickListener {

    private lateinit var binding: ActivityLessonDetailBinding
    private lateinit var edufunDatabaseHelper: EdufunDatabaseHelper
    private lateinit var chapterAdapter: ChapterAdapter

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLessonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        val categoryId = intent.getIntExtra("category_id", 0)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, categoryId)

        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

        edufunDatabaseHelper = EdufunDatabaseHelper(this)

        val lessonTitle = intent.getStringExtra("lesson_title") ?: "Mata Pelajaran"
        val lessonDesc = intent.getStringExtra("lesson_desc") ?: "Deskripsi Mata Pelajaran"
        val imageResId = intent.getIntExtra("image_res_id", 0)

        binding.tvTitle.text = lessonTitle
        binding.tvDesc.text = lessonDesc

        if (imageResId != 0) {
            binding.posterImageIv.setImageResource(imageResId)
        } else {
            Log.w("LessonDetailActivity", "Image resource ID is not provided or is 0")
        }

        setupView()
    }

    override fun onChapterClick(chapter: Chapter) {
        Log.d("LessonDetailActivity", "Chapter clicked: ${chapter.title}")

        val intent = Intent(this, ChapterDetailActivity::class.java)
        intent.putExtra("chapter_title", chapter.title)
        intent.putExtra("chapter_desc", chapter.description)
        intent.putExtra("category_id", chapter.categoryId)
        intent.putExtra("video_name", chapter.video)
        startActivity(intent)
        finish()
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
