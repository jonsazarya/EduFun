package com.example.edufun

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edufun.adapter.CategoryAdapter
import com.example.edufun.database.DatabaseHelper
import com.example.edufun.databinding.ActivityHomeBinding
import com.example.edufun.model.Category

class HomeActivity : AppCompatActivity(), CategoryAdapter.OnCategoryClickListener {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi DatabaseHelper
        dbHelper = DatabaseHelper(this)

        // Menambahkan data pelajaran ke database
        addSampleData()

        // Mengambil semua mata pelajaran dari database
        val categories = dbHelper.getAllCategory()

        // Mengatur adapter
        val adapter = CategoryAdapter(categories, this)
        binding.rvCategory.adapter = adapter
        binding.rvCategory.layoutManager = LinearLayoutManager(this)
    }

    private fun addSampleData() {
        // Menambahkan data pelajaran jika belum ada
        dbHelper.addCategory("Matematika", "Belajar Matematika sangat menyenangkan!", R.drawable.math)
        dbHelper.addCategory("IPA", "Belajar IPA sangat menyenangkan!", R.drawable.science)
        dbHelper.addCategory("Seni Budaya", "Belajar Seni Budaya sangat menyenangkan!", R.drawable.seni_budaya)
    }

    // Implementasi metode dari listener
    override fun onCategoryClick(category: Category) {
        // Tindakan yang diambil saat kategori diklik
        Toast.makeText(this, "Kategori: ${category.name}", Toast.LENGTH_SHORT).show()
    }
}
