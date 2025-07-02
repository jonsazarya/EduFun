package com.example.edufun.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.edufun.R
import com.example.edufun.model.QuizDetail

class QuizDetailAdapter(
    private var quizList: List<QuizDetail>?,
    private val context: Context,
    private val onAnswerSelected: (QuizDetail, String) -> Unit
) : RecyclerView.Adapter<QuizDetailAdapter.QuizViewHolder>() {

    inner class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvQuestion: TextView = itemView.findViewById(R.id.tvQuestion)
        val radioGroup: RadioGroup = itemView.findViewById(R.id.radioGroup)
        val rbOption1: RadioButton = itemView.findViewById(R.id.rbOption1)
        val rbOption2: RadioButton = itemView.findViewById(R.id.rbOption2)
        val rbOption3: RadioButton = itemView.findViewById(R.id.rbOption3)
        val rbOption4: RadioButton = itemView.findViewById(R.id.rbOption4)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_quiz_detail, parent, false)
        return QuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val quiz = quizList?.get(position) ?: return

        holder.tvQuestion.text = quiz.question

        // Split options by delimiter
        val options = quiz.options.split("|").map { it.trim() }

        // Assign options to radio buttons
        holder.rbOption1.text = options.getOrNull(0) ?: ""
        holder.rbOption2.text = options.getOrNull(1) ?: ""
        holder.rbOption3.text = options.getOrNull(2) ?: ""
        holder.rbOption4.text = options.getOrNull(3) ?: ""

        holder.radioGroup.setOnCheckedChangeListener(null) // prevent triggering when recycled

        holder.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedOption = when (checkedId) {
                holder.rbOption1.id -> holder.rbOption1.text.toString()
                holder.rbOption2.id -> holder.rbOption2.text.toString()
                holder.rbOption3.id -> holder.rbOption3.text.toString()
                holder.rbOption4.id -> holder.rbOption4.text.toString()
                else -> ""
            }
            onAnswerSelected(quiz, selectedOption)
        }
    }

    override fun getItemCount(): Int {
        return quizList?.size ?: 0
    }
}
