package com.example.edufun.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.edufun.R
import com.example.edufun.model.Quiz

class QuizAdapter(
    private val quizzes: List<Quiz>,
    private val listener: OnQuizClickListener
) : RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    interface OnQuizClickListener {
        fun onQuizClick(quiz: Quiz)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_quiz, parent, false)
        return QuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val quiz = quizzes[position]
        holder.bind(quiz)
    }

    override fun getItemCount(): Int = quizzes.size

    inner class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvQuizTitle: TextView = itemView.findViewById(R.id.tv_quiz_title)
        private val tvQuizContent: TextView = itemView.findViewById(R.id.tv_quiz_description)

        fun bind(quiz: Quiz) {
            tvQuizTitle.text = quiz.title
            tvQuizContent.text = quiz.desc

            itemView.setOnClickListener {
                listener.onQuizClick(quiz)
            }
        }
    }
}
