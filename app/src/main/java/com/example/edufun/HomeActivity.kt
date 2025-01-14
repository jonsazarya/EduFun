package com.example.edufun

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edufun.adapter.CategoryAdapter
import com.example.edufun.databinding.ActivityHomeBinding
import com.example.edufun.model.Category

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val categories = listOf(
            Category("Matematika", "Belajar Matematika sangat menyenangkan!", R.drawable.math),
            Category("IPA", "Belajar IPA sangat menyenangkan!", R.drawable.science),
            Category("Seni Budaya", "Belajar Seni Budaya sangat menyenangkan!", R.drawable.seni_budaya)
        )

        val adapter = CategoryAdapter(categories)
        binding.rvCategory.adapter = adapter
        binding.rvCategory.layoutManager = LinearLayoutManager(this)
    }
}