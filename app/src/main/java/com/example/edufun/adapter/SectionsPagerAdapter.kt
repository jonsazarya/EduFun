package com.example.edufun.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.edufun.view.chapter.ChapterFragment
import com.example.edufun.view.quiz.QuizFragment

class SectionsPagerAdapter(fragmentActivity: FragmentActivity, private val categoryId: Int) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            0 -> ChapterFragment()
            1 -> QuizFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }

        // Mengirim categoryId ke ChapterFragment dan QuizFragment
        val args = Bundle()
        args.putInt("category_id", categoryId)
        fragment.arguments = args

        return fragment
    }
}
