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
        private const val TABLE_NAME = "quizzes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_CATEGORY_ID = "categoryId"
        private const val COLUMN_QUESTION = "question"
        private const val COLUMN_OPTION1 = "option1"
        private const val COLUMN_OPTION2 = "option2"
        private const val COLUMN_OPTION3 = "option3"
        private const val COLUMN_OPTION4 = "option4"
        private const val COLUMN_ANSWER = "answer"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_CATEGORY_ID INTEGER, " +
                "$COLUMN_QUESTION TEXT, " +
                "$COLUMN_OPTION1 TEXT, " +
                "$COLUMN_OPTION2 TEXT, " +
                "$COLUMN_OPTION3 TEXT, " +
                "$COLUMN_OPTION4 TEXT, " +
                "$COLUMN_ANSWER TEXT" +
                ")")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addQuiz(categoryId: Int, question: String, option1: String, option2: String, option3: String, option4: String, answer: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CATEGORY_ID, categoryId)
            put(COLUMN_QUESTION, question)
            put(COLUMN_OPTION1, option1)
            put(COLUMN_OPTION2, option2)
            put(COLUMN_OPTION3, option3)
            put(COLUMN_OPTION4, option4)
            put(COLUMN_ANSWER, answer)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getQuizzesByCategoryId(categoryId: Int): List<Quiz> {
        val quizList = mutableListOf<Quiz>()
        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_CATEGORY_ID = $categoryId", null)

        cursor.use { cursor ->
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val quiz = Quiz(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUESTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION1)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION2)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION3)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION4)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ANSWER))
                    )
                    quizList.add(quiz)
                } while (cursor.moveToNext())
            }
        }
        db.close()
        return quizList
    }
}
