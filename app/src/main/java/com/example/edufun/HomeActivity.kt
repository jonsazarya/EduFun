package com.example.edufun

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edufun.adapter.CategoryAdapter
import com.example.edufun.database.CategoryDatabaseHelper
import com.example.edufun.databinding.ActivityHomeBinding
import com.example.edufun.model.Category

class HomeActivity : AppCompatActivity(), CategoryAdapter.OnCategoryClickListener {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var categoryDatabaseHelper: CategoryDatabaseHelper
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        categoryDatabaseHelper = CategoryDatabaseHelper(this)
        categoryDatabaseHelper.addCategory("Matematika", "Matematika itu menyenangkan", R.drawable.math)
        categoryDatabaseHelper.addCategory("IPA", "IPA itu menyenangkan", R.drawable.science)
        categoryDatabaseHelper.addCategory("Seni Budaya", "Seni Budaya itu menyenangkan", R.drawable.seni_budaya)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val categories = categoryDatabaseHelper.getAllCategory()

        categoryAdapter = CategoryAdapter(categories, this)
        binding.rvCategory.layoutManager = LinearLayoutManager(this)
        binding.rvCategory.adapter = categoryAdapter
    }

    override fun onCategoryClick(category: Category) {
        val intent = Intent(this, LessonDetailActivity::class.java)
        intent.putExtra("lesson_title", category.name)
        intent.putExtra("lesson_desc", category.desc)
        intent.putExtra("image_res_id", category.imageResId) // Corrected key name
        startActivity(intent)
    }
}
