package com.example.edufun.view.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edufun.adapter.HistoryAdapter
import com.example.edufun.database.EdufunDatabaseHelper
import com.example.edufun.databinding.FragmentHistoryBinding
import com.example.edufun.model.History
import com.example.edufun.pref.UserPreference
import com.example.edufun.pref.dataStore
import com.example.edufun.repo.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var userRepository: UserRepository
    private lateinit var edufunDatabaseHelper: EdufunDatabaseHelper
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

        // ✅ Inisialisasi repository dan database helper
        userRepository = UserRepository.getInstance(
            UserPreference.getInstance(requireContext().dataStore)
        )
        edufunDatabaseHelper = EdufunDatabaseHelper(requireContext())

        // Setup RecyclerView
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
        lifecycleScope.launch {
            userRepository = UserRepository.getInstance(
                UserPreference.getInstance(requireContext().dataStore)
            )
            edufunDatabaseHelper = EdufunDatabaseHelper(requireContext())

            val user = userRepository.getSession().first()
            val userId = user.id

            val fetchedHistory = withContext(Dispatchers.IO) {
                edufunDatabaseHelper.getQuizHistoryByUserId(userId)
            }

            Log.d("HISTORY_DEBUG", "Data dari DB: ${fetchedHistory.size}")

            historyList.clear()
            historyList.addAll(fetchedHistory)
            historyAdapter.notifyDataSetChanged()

            if (historyList.isEmpty()) {
                binding.rvHistory.visibility = View.GONE
                binding.tvNotFound.visibility = View.VISIBLE
            } else {
                binding.rvHistory.visibility = View.VISIBLE
                binding.tvNotFound.visibility = View.GONE
            }
        }
    }

    private fun deleteHistoryItem(position: Int) {
        val quizToDelete = historyList[position]

        lifecycleScope.launch(Dispatchers.IO) {
            edufunDatabaseHelper.deleteQuizHistoryById(quizToDelete.id)

            withContext(Dispatchers.Main) {
                historyList.removeAt(position)
                historyAdapter.updateData(historyList)
                Toast.makeText(requireContext(), "History deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
