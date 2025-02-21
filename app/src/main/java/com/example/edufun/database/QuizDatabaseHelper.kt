package com.example.edufun.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.edufun.model.Quiz

class QuizDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "quiz.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_QUIZZES = "quizzes"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_CATEGORY_ID = "category_id"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_QUIZZES (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_TITLE TEXT, " +
                "$COLUMN_CONTENT TEXT, " +
                "$COLUMN_CATEGORY_ID INTEGER)")
            .trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_QUIZZES")
        onCreate(db)
    }

    fun addQuiz(title: String, content: String, categoryId: Int): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, title)
            put(COLUMN_CONTENT, content)
            put(COLUMN_CATEGORY_ID, categoryId)
        }
        val quizId = db.insert(TABLE_QUIZZES, null, values).toInt()
        db.close()
        return quizId
    }

    fun getAllQuizzesByCategoryId(categoryId: Int): List<Quiz> {
        val quizzes = mutableListOf<Quiz>()
        val db = this.readableDatabase
        val selection = "$COLUMN_CATEGORY_ID = ?"
        val selectionArgs = arrayOf(categoryId.toString())
        val cursor: Cursor? = db.query(
            TABLE_QUIZZES,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID))
                val title = it.getString(it.getColumnIndexOrThrow(COLUMN_TITLE))
                val content = it.getString(it.getColumnIndexOrThrow(COLUMN_CONTENT))
                quizzes.add(Quiz(id, title, content, categoryId))
            }
        }
        return quizzes
    }
}
