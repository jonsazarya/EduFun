package com.example.edufun

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.edufun.adapter.ChapterAdapter
import com.example.edufun.adapter.SectionsPagerAdapter
import com.example.edufun.database.ChapterDatabaseHelper
import com.example.edufun.databinding.ActivityLessonDetailBinding
import com.example.edufun.model.Chapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class LessonDetailActivity : AppCompatActivity(), ChapterAdapter.OnChapterClickListener {

    private lateinit var binding: ActivityLessonDetailBinding
    private lateinit var chapterDatabaseHelper: ChapterDatabaseHelper
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

        val categoryId = intent.getIntExtra("category_id", 0)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, categoryId)

        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

        chapterDatabaseHelper = ChapterDatabaseHelper(this)

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
