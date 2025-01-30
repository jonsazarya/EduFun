package com.example.edufun.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.edufun.model.Category

class CategoryDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "mataPelajaran.db"
        private const val DATABASE_VERSION = 2
        private const val TABLE_CATEGORIES = "categories"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_IMAGE_RES_ID = "imageResId"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_CATEGORIES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_DESCRIPTION TEXT NOT NULL,
                $COLUMN_IMAGE_RES_ID INTEGER NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORIES")
        onCreate(db)
    }

    fun addCategory(name: String, description: String, imageResId: Int): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_DESCRIPTION, description)
            put(COLUMN_IMAGE_RES_ID, imageResId)
        }
        val categoryId = db.insert(TABLE_CATEGORIES, null, values).toInt()
        db.close()
        return categoryId
    }

    fun getAllCategories(): List<Category> {
        val categories = mutableListOf<Category>()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_CATEGORIES"
        val cursor: Cursor? = db.rawQuery(selectQuery, null)

        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID))
                val name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME))
                val description = it.getString(it.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                val imageResId = it.getInt(it.getColumnIndexOrThrow(COLUMN_IMAGE_RES_ID))
                categories.add(Category(id, name, description, imageResId))
            }
        }
        cursor?.close()
        db.close()
        return categories
    }

    fun getCategoryById(categoryId: Int): Category? {
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_CATEGORIES WHERE $COLUMN_ID = $categoryId"
        val cursor: Cursor? = db.rawQuery(selectQuery, null)

        val category = cursor?.use {
            if (it.moveToFirst()) {
                val id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID))
                val name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME))
                val description = it.getString(it.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                val imageResId = it.getInt(it.getColumnIndexOrThrow(COLUMN_IMAGE_RES_ID))
                Category(id, name, description, imageResId)
            } else {
                null
            }
        }
        cursor?.close()
        db.close()
        return category
    }
}
