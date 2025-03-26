package com.example.edufun.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.edufun.R
import com.example.edufun.model.History

class HistoryAdapter(private val quizList: List<History>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private var onDeleteClickListener: OnDeleteClickListener? = null

    interface OnDeleteClickListener {
        fun onDeleteClick(position: Int)
    }

    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
        onDeleteClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_quiz_history_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = quizList[position]
        if (currentItem.score.isNotEmpty()) {
            holder.bind(currentItem)
            holder.itemView.visibility = View.VISIBLE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT) // Atur kembali parameter tata letak
        } else {
            holder.itemView.visibility = View.GONE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
        }
    }

    override fun getItemCount() = quizList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val quizTitle: TextView = itemView.findViewById(R.id.quiz_title)
        val quizScore: TextView = itemView.findViewById(R.id.quiz_score)
        val deleteButton: Button = itemView.findViewById(R.id.btnDelete)

        fun bind(quiz: History) {
            quizTitle.text = quiz.title
            quizScore.text = quiz.score

            deleteButton.setOnClickListener {
                onDeleteClickListener?.onDeleteClick(adapterPosition)
            }
        }
    }
}
