package com.example.edufun

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edufun.adapter.CategoryAdapter
import com.example.edufun.database.EdufunDatabaseHelper
import com.example.edufun.databinding.FragmentHomeBinding
import com.example.edufun.model.Category
import com.example.edufun.model.User
import com.example.edufun.view.ViewModelFactory
import com.example.edufun.view.lesson.LessonDetailActivity
import com.example.edufun.view.main.MainViewModel
import java.util.Locale

class HomeFragment : Fragment(), CategoryAdapter.OnCategoryClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var edufunDatabaseHelper: EdufunDatabaseHelper
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var categoryAdapter: CategoryAdapter
    private var categories: List<Category> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        edufunDatabaseHelper = EdufunDatabaseHelper(requireContext())

        edufunDatabaseHelper.readableDatabase

        mainViewModel.getSession().observe(viewLifecycleOwner) { user ->
            updateUI(user)
        }

        addSampleDataIfNeeded()
        setupRecyclerView()
        setupSearchView()

        return binding.root
    }

    private fun addSampleDataIfNeeded() {
        val existingCategories = edufunDatabaseHelper.getAllCategories()
        if (existingCategories.isEmpty()) {
            edufunDatabaseHelper.seedFromJson(requireContext())
        }
    }

    private fun setupRecyclerView() {
        binding.progressBar.visibility = View.VISIBLE

        // Load langsung tanpa delay
        categories = edufunDatabaseHelper.getAllCategories()
        categoryAdapter = CategoryAdapter(categories, this)
        binding.rvCategory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = categoryAdapter
        }

        binding.progressBar.visibility = View.GONE
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterCategories(newText)
                return true
            }
        })
    }

    private fun filterCategories(query: String?) {
        query?.let {
            val filteredList = categories.filter {
                it.name.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT))
            }

            if (filteredList.isEmpty()) {
                binding.rvCategory.visibility = View.GONE
                Toast.makeText(requireContext(), "Pelajaran tidak ditemukan", Toast.LENGTH_SHORT).show()
            } else {
                binding.rvCategory.visibility = View.VISIBLE
                categoryAdapter.updateList(filteredList)
            }
        }
    }

    private fun updateUI(user: User) {
        binding.tvUsername.text = user.email.ifEmpty { "Hai! User" }
    }

    override fun onCategoryClick(category: Category) {
        val intent = Intent(requireContext(), LessonDetailActivity::class.java).apply {
            putExtra("lesson_title", category.name)
            putExtra("lesson_desc", category.desc)
            putExtra("image_res_id", category.imageResId)
            putExtra("category_id", category.id)
        }
        startActivity(intent)
    }
}
