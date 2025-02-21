package com.example.edufun.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.edufun.model.Chapter

class ChapterDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "chapter.db"
        private const val DATABASE_VERSION = 2
        private const val TABLE_CHAPTERS = "chapters"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_CATEGORY_ID = "categoryId"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_CHAPTERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT NOT NULL,
                $COLUMN_DESCRIPTION TEXT NOT NULL,
                $COLUMN_CATEGORY_ID INTEGER NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CHAPTERS")
        onCreate(db)
    }

    fun addChapter(title: String, description: String, categoryId: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, title)
            put(COLUMN_DESCRIPTION, description)
            put(COLUMN_CATEGORY_ID, categoryId)
        }
        db.insert(TABLE_CHAPTERS, null, values)
        db.close()
    }

    fun getAllChaptersByCategoryId(categoryId: Int): List<Chapter> {
        val chapters = mutableListOf<Chapter>()
        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT * FROM $TABLE_CHAPTERS WHERE $COLUMN_CATEGORY_ID = ?", arrayOf(categoryId.toString()))

        cursor.use { cursor ->
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val chapter = Chapter(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID))
                    )
                    chapters.add(chapter)
                } while (cursor.moveToNext())
            }
        }
        db.close()
        return chapters
    }
}
