package com.example.edufun.view.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edufun.R
import com.example.edufun.adapter.HistoryAdapter
import com.example.edufun.database.AppDatabase
import com.example.edufun.databinding.FragmentHistoryBinding
import com.example.edufun.model.History
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var historyAdapter: HistoryAdapter
    private var historyList: MutableList<History> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        historyAdapter = HistoryAdapter(historyList)
        binding.rvHistory.adapter = historyAdapter

        loadHistory()

        historyAdapter.setOnDeleteClickListener(object : HistoryAdapter.OnDeleteClickListener {
            override fun onDeleteClick(position: Int) {
                deleteHistoryItem(position)
            }
        })
    }

    private fun loadHistory() {
        GlobalScope.launch(Dispatchers.IO) {
            val database = AppDatabase.getDatabase(requireContext())
            val fetchedHistory = database.quizHistoryDao().getAllQuiz()

            withContext(Dispatchers.Main) {
                historyList.clear()
                historyList.addAll(fetchedHistory)
                historyAdapter.notifyDataSetChanged()

                if (historyList.isEmpty()){
                    binding.rvHistory.visibility = View.GONE
                    binding.tvNotFound.visibility = View.VISIBLE
                } else {
                    binding.rvHistory.visibility = View.VISIBLE
                    binding.tvNotFound.visibility = View.GONE
                }
            }
        }
    }

    private fun deleteHistoryItem(position: Int) {
        val quizToDelete = historyList[position]
        GlobalScope.launch(Dispatchers.IO) {
            val database = AppDatabase.getDatabase(requireContext())
            database.quizHistoryDao().deleteQuiz(quizToDelete)

            withContext(Dispatchers.Main) {
                historyList.removeAt(position)
                historyAdapter.notifyItemRemoved(position)
                Toast.makeText(requireContext(), "History deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }
}