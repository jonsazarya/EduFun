package com.example.edufun.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.edufun.model.QuizDetail
import java.math.BigInteger

class QuizDetailDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "quizDetail.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_QUIZZES_DETAIL = "quizzes_detail"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_QUIZ_ID = "quiz_id"
        private const val COLUMN_QUESTION = "question"
        private const val COLUMN_OPTION1 = "option1"
        private const val COLUMN_OPTION2 = "option2"
        private const val COLUMN_OPTION3 = "option3"
        private const val COLUMN_OPTION4 = "option4"
        private const val COLUMN_ANSWER = "answer"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createQuizzesDetailTable = """
        CREATE TABLE $TABLE_QUIZZES_DETAIL (
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_QUIZ_ID INTEGER,
            $COLUMN_QUESTION STRING,
            $COLUMN_OPTION1 STRING,
            $COLUMN_OPTION2 STRING,
            $COLUMN_OPTION3 STRING,
            $COLUMN_OPTION4 STRING,
            $COLUMN_ANSWER STRING
        )
    """.trimIndent()
        db.execSQL(createQuizzesDetailTable)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_QUIZZES_DETAIL")
        onCreate(db)
    }

    fun addQuizDetail(quizId: Int, question: String, option1: String, option2: String, option3: String, option4: String, answer: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_QUIZ_ID, quizId)
            put(COLUMN_QUESTION, question)
            put(COLUMN_OPTION1, option1)
            put(COLUMN_OPTION2, option2)
            put(COLUMN_OPTION3, option3)
            put(COLUMN_OPTION4, option4)
            put(COLUMN_ANSWER, answer)
        }
        val quizDetailId = db.insert(TABLE_QUIZZES_DETAIL, null, values).toInt()
        db.close()
        return quizDetailId
    }

    fun getAllQuizzesByQuizId(quizId: Int): LiveData<List<QuizDetail>> {
        val quizzesLiveData = MutableLiveData<List<QuizDetail>>()
        val quizList = mutableListOf<QuizDetail>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_QUIZZES_DETAIL WHERE $COLUMN_QUIZ_ID = $quizId"
        val cursor: Cursor?
        try {
            cursor = db.rawQuery(query, null)
        } catch (e: Exception) {
            db.close()
            quizzesLiveData.value = emptyList()
            return quizzesLiveData
        }

        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID))
                    val question = it.getString(it.getColumnIndexOrThrow(COLUMN_QUESTION))
                    val option1 = it.getString(it.getColumnIndexOrThrow(COLUMN_OPTION1))
                    val option2 = it.getString(it.getColumnIndexOrThrow(COLUMN_OPTION2))
                    val option3 = it.getString(it.getColumnIndexOrThrow(COLUMN_OPTION3))
                    val option4 = it.getString(it.getColumnIndexOrThrow(COLUMN_OPTION4))
                    val answer = it.getString(it.getColumnIndexOrThrow(COLUMN_ANSWER))

                    val quizDetail = QuizDetail(id, quizId, question, option1, option2, option3, option4, answer)
                    quizList.add(quizDetail)
                } while (it.moveToNext())
            }
        }

        db.close()
        quizzesLiveData.value = quizList
        return quizzesLiveData
    }
}
