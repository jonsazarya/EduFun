package com.example.edufun.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.edufun.R
import com.example.edufun.model.Category
import com.example.edufun.model.Chapter

class ChapterAdapter(private val chapters: List<Chapter>, private val listener: OnChapterClickListener) : RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder>() {

    interface OnChapterClickListener {
        fun onChapterClick(chapter: Chapter)
    }

    inner class ChapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chapterName: TextView = itemView.findViewById(R.id.tv_chapter_title)
        val chapterDescription: TextView = itemView.findViewById(R.id.tv_chapter_description)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_chapter, parent, false)
        return ChapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
        val chapter = chapters[position]
        holder.chapterName.text = chapter.title
        holder.chapterDescription.text = chapter.description

        holder.itemView.setOnClickListener {
            listener.onChapterClick(chapter)
        }
    }

    override fun getItemCount(): Int {
        return chapters.size
    }
}
