package com.example.edufun.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.edufun.model.Category
import com.example.edufun.model.Chapter
import com.example.edufun.model.History
import com.example.edufun.model.Quiz
import com.example.edufun.model.QuizDetail
import com.example.edufun.model.User
import org.json.JSONObject

class EdufunDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "edufun.db"
        private const val DATABASE_VERSION = 2
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_USER_TABLE)
        db?.execSQL(CREATE_CATEGORY_TABLE)
        db?.execSQL(CREATE_CHAPTER_TABLE)
        db?.execSQL(CREATE_QUIZ_TABLE)
        db?.execSQL(CREATE_QUIZ_DETAIL_TABLE)
        db?.execSQL(CREATE_HISTORY_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS users")
        db?.execSQL("DROP TABLE IF EXISTS categories")
        db?.execSQL("DROP TABLE IF EXISTS chapters")
        db?.execSQL("DROP TABLE IF EXISTS quizzes")
        db?.execSQL("DROP TABLE IF EXISTS quiz_details")
        db?.execSQL("DROP TABLE IF EXISTS quiz_history")
        onCreate(db)
    }

    private val CREATE_USER_TABLE = """
        CREATE TABLE users (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT,
            email TEXT UNIQUE,
            password TEXT
        )
    """.trimIndent()

    private val CREATE_CATEGORY_TABLE = """
        CREATE TABLE categories (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT,
            description TEXT,
            image_res_id INTEGER
        )
    """.trimIndent()

    private val CREATE_CHAPTER_TABLE = """
        CREATE TABLE chapters (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            title TEXT,
            description TEXT,
            category_id INTEGER,
            video TEXT,
            FOREIGN KEY (category_id) REFERENCES categories(id)
        )
    """.trimIndent()

    private val CREATE_QUIZ_TABLE = """
        CREATE TABLE quizzes (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            title TEXT,
            description TEXT,
            category_id INTEGER,
            FOREIGN KEY (category_id) REFERENCES categories(id)
        )
    """.trimIndent()

    private val CREATE_QUIZ_DETAIL_TABLE = """
        CREATE TABLE quiz_details (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            quiz_id INTEGER,
            question TEXT,
            options TEXT,
            answer TEXT,
            FOREIGN KEY (quiz_id) REFERENCES quizzes(id)
        )
    """.trimIndent()

    private val CREATE_HISTORY_TABLE = """
        CREATE TABLE quiz_history (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            title TEXT,
            score TEXT,
            user_id INTEGER,
            quiz_id INTEGER,
            FOREIGN KEY (user_id) REFERENCES users(id),
            FOREIGN KEY (quiz_id) REFERENCES quizzes(id)
        )
    """.trimIndent()

    fun seedFromJson(context: Context) {
        val jsonStr = context.assets.open("seed_data.json").bufferedReader().use { it.readText() }
        val jsonObject = JSONObject(jsonStr)
        val categoriesArray = jsonObject.getJSONArray("categories")

        for (i in 0 until categoriesArray.length()) {
            val categoryObj = categoriesArray.getJSONObject(i)
            val name = categoryObj.getString("name")
            val desc = categoryObj.getString("description")
            val imageResName = categoryObj.getString("image_res_id")
            val imageResId = context.resources.getIdentifier(imageResName, "drawable", context.packageName)
            val categoryId = addCategory(name, desc, imageResId)

            val chapters = categoryObj.getJSONArray("chapters")
            for (j in 0 until chapters.length()) {
                val chapter = chapters.getJSONObject(j)
                addChapter(
                    chapter.getString("title"),
                    chapter.getString("description"),
                    categoryId,
                    chapter.getString("video")
                )
            }

            val quizzes = categoryObj.getJSONArray("quizzes")
            for (k in 0 until quizzes.length()) {
                val quiz = quizzes.getJSONObject(k)
                val quizId = addQuiz(quiz.getString("title"), quiz.getString("description"), categoryId)

                val questions = quiz.getJSONArray("questions")
                for (q in 0 until questions.length()) {
                    val question = questions.getJSONObject(q)
                    val optionsArray = question.getJSONArray("options")
                    val optionsList = mutableListOf<String>()
                    for (o in 0 until optionsArray.length()) {
                        optionsList.add(optionsArray.getString(o))
                    }
                    val optionsJoined = optionsList.joinToString("|")
                    addQuizDetail(quizId, question.getString("question"), optionsJoined, question.getString("answer"))
                }
            }
        }
    }

    fun insertUser(name: String, email: String, password: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("name", name)
            put("email", email)
            put("password", password)
        }
        val result = db.insert("users", null, values)
        return result
    }

    fun readUser(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val selection = "email = ? AND password = ?"
        val selectionArgs = arrayOf(email, password)
        val cursor = db.query("users", null, selection, selectionArgs, null, null, null)
        val userExist = cursor.count > 0
        cursor.close()
        return userExist
    }

    fun isEmailExist(email: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query("users", null, "email = ?", arrayOf(email), null, null, null)
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun addCategory(name: String, description: String, imageResId: Int): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("name", name)
            put("description", description)
            put("image_res_id", imageResId)
        }
        val categoryId = db.insert("categories", null, values).toInt()
        return categoryId
    }

    fun getAllCategories(): List<Category> {
        val categories = mutableListOf<Category>()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM categories"
        val cursor: Cursor? = db.rawQuery(selectQuery, null)

        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow("id"))
                val name = it.getString(it.getColumnIndexOrThrow("name"))
                val description = it.getString(it.getColumnIndexOrThrow("description"))
                val imageResId = it.getInt(it.getColumnIndexOrThrow("image_res_id"))
                categories.add(Category(id, name, description, imageResId))
            }
        }
        cursor?.close()
        return categories
    }

    fun getCategoryById(categoryId: Int): Category? {
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM categories WHERE id = $categoryId"
        val cursor: Cursor? = db.rawQuery(selectQuery, null)

        val category = cursor?.use {
            if (it.moveToFirst()) {
                val id = it.getInt(it.getColumnIndexOrThrow("id"))
                val name = it.getString(it.getColumnIndexOrThrow("name"))
                val description = it.getString(it.getColumnIndexOrThrow("description"))
                val imageResId = it.getInt(it.getColumnIndexOrThrow("image_res_id"))
                Category(id, name, description, imageResId)
            } else {
                null
            }
        }
        cursor?.close()
        return category
    }

    fun addChapter(title: String, description: String, categoryId: Int, video: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("title", title)
            put("description", description)
            put("category_id", categoryId)
            put("video", video)
        }
        db.insert("chapters", null, values)
    }

    fun getAllChaptersByCategoryId(categoryId: Int): List<Chapter> {
        val chapters = mutableListOf<Chapter>()
        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT * FROM chapters WHERE category_id = ?", arrayOf(categoryId.toString()))

        cursor.use {
            if (it != null && it.moveToFirst()) {
                do {
                    val chapter = Chapter(
                        it.getInt(it.getColumnIndexOrThrow("id")),
                        it.getString(it.getColumnIndexOrThrow("title")),
                        it.getString(it.getColumnIndexOrThrow("description")),
                        it.getInt(it.getColumnIndexOrThrow("category_id")),
                        it.getString(it.getColumnIndexOrThrow("video"))
                    )
                    chapters.add(chapter)
                } while (it.moveToNext())
            }
        }
        return chapters
    }

    fun addQuiz(title: String, content: String, categoryId: Int): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("title", title)
            put("description", content)
            put("category_id", categoryId)
        }
        val quizId = db.insert("quizzes", null, values).toInt()
        return quizId
    }

    fun getAllQuizzesByCategoryId(categoryId: Int): List<Quiz> {
        val quizzes = mutableListOf<Quiz>()
        val db = this.readableDatabase
        val cursor = db.query(
            "quizzes",
            null,
            "category_id = ?",
            arrayOf(categoryId.toString()),
            null,
            null,
            null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow("id"))
                val title = it.getString(it.getColumnIndexOrThrow("title"))
                val description = it.getString(it.getColumnIndexOrThrow("description"))
                quizzes.add(Quiz(id, title, description, categoryId))
            }
        }
        cursor?.close()
        return quizzes
    }

    fun addQuizDetail(quizId: Int, question: String, options: String, answer: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("quiz_id", quizId)
            put("question", question)
            put("options", options)
            put("answer", answer)
        }
        val quizDetailId = db.insert("quiz_details", null, values).toInt()
        return quizDetailId
    }

    fun getAllQuizDetailsByQuizId(quizId: Int): LiveData<List<QuizDetail>> {
        val quizzesLiveData = MutableLiveData<List<QuizDetail>>()
        val quizList = mutableListOf<QuizDetail>()
        val db = this.readableDatabase
        val query = "SELECT * FROM quiz_details WHERE quiz_id = $quizId"
        val cursor: Cursor? = db.rawQuery(query, null)

        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    val id = it.getInt(it.getColumnIndexOrThrow("id"))
                    val question = it.getString(it.getColumnIndexOrThrow("question"))
                    val options = it.getString(it.getColumnIndexOrThrow("options"))
                    val answer = it.getString(it.getColumnIndexOrThrow("answer"))
                    val quizDetail = QuizDetail(id, quizId, question, options, answer)
                    quizList.add(quizDetail)
                } while (it.moveToNext())
            }
        }

        quizzesLiveData.value = quizList
        return quizzesLiveData
    }

    fun insertQuizHistory(title: String, score: String, userId: Int, quizId: Int): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("title", title)
            put("score", score)
            put("user_id", userId)
            put("quiz_id", quizId)
        }
        val result = db.insert("quiz_history", null, values)
        Log.d("QUIZ_INSERT", "Inserted with result ID: $result")
        return result
    }

    fun getQuizHistoryByUserId(userId: Int): List<History> {
        val historyList = mutableListOf<History>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM quiz_history WHERE user_id = ?", arrayOf(userId.toString()))
        Log.d("DB_QUERY", "Querying history for user_id=$userId")

        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val id = it.getLong(it.getColumnIndexOrThrow("id"))
                    val title = it.getString(it.getColumnIndexOrThrow("title"))
                    val score = it.getString(it.getColumnIndexOrThrow("score"))
                    val quizId = it.getInt(it.getColumnIndexOrThrow("quiz_id"))
                    val uid = it.getInt(it.getColumnIndexOrThrow("user_id"))
                    Log.d("DB_RESULT", "id=$id title=$title score=$score quizId=$quizId uid=$uid")

                    historyList.add(History(id, title, score, uid, quizId))
                } while (it.moveToNext())
            }
        }

        return historyList
    }

    fun deleteQuizHistoryById(id: Long): Int {
        val db = this.writableDatabase
        return db.delete("quiz_history", "id = ?", arrayOf(id.toString()))
    }

    fun getUserByEmail(email: String): User? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", arrayOf(email))

        cursor.use {
            if (it.moveToFirst()) {
                val id = it.getInt(it.getColumnIndexOrThrow("id"))
                val emailVal = it.getString(it.getColumnIndexOrThrow("email"))
                val password = it.getString(it.getColumnIndexOrThrow("password"))
                return User(id, emailVal, password, true)
            }
        }
        return null
    }

    fun getRandomQuizDetailsByQuizId(quizId: Int, limit: Int = 5): LiveData<List<QuizDetail>> {
        val quizzesLiveData = MutableLiveData<List<QuizDetail>>()
        val quizList = mutableListOf<QuizDetail>()
        val db = this.readableDatabase

        val query = "SELECT * FROM quiz_details WHERE quiz_id = ? ORDER BY RANDOM() LIMIT ?"
        val cursor = db.rawQuery(query, arrayOf(quizId.toString(), limit.toString()))

        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val id = it.getInt(it.getColumnIndexOrThrow("id"))
                    val question = it.getString(it.getColumnIndexOrThrow("question"))
                    val options = it.getString(it.getColumnIndexOrThrow("options"))
                    val answer = it.getString(it.getColumnIndexOrThrow("answer"))

                    val quizDetail = QuizDetail(id, quizId, question, options, answer)
                    quizList.add(quizDetail)
                } while (it.moveToNext())
            }
        }

        db.close()
        quizzesLiveData.value = quizList
        return quizzesLiveData
    }
}
