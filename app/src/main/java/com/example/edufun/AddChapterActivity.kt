package com.example.edufun

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.edufun.database.ChapterDatabaseHelper

class AddChapterActivity : AppCompatActivity() {

    private lateinit var dbHelper: ChapterDatabaseHelper
    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var videoUrlEditText: EditText
    private lateinit var addButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_chapter) // Pastikan Anda memiliki layout ini

        dbHelper = ChapterDatabaseHelper(this)
        titleEditText = findViewById(R.id.editTextTitle)
        descriptionEditText = findViewById(R.id.editTextDescription)
        videoUrlEditText = findViewById(R.id.editTextVideoUrl)
        addButton = findViewById(R.id.buttonAdd)

        addButton.setOnClickListener {
            addChapter()
        }
    }

    private fun addChapter() {
        val title = titleEditText.text.toString()
        val description = descriptionEditText.text.toString()
        val videoUrl = videoUrlEditText.text.toString()

        if (title.isNotEmpty() && description.isNotEmpty() && videoUrl.isNotEmpty()) {
            dbHelper.addChapter(title, description, videoUrl)
            Toast.makeText(this, "Bab berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            finish() // Kembali ke Activity sebelumnya
        } else {
            Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
        }
    }
}
