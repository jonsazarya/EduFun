package com.example.edufun.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.edufun.model.Chapter

class ChapterDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "mataPelajaran.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "chapters"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_VIDEO_URL = "video_url"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_TITLE TEXT, " +
                "$COLUMN_DESCRIPTION TEXT" +
                "$COLUMN_VIDEO_URL TEXT" +
                ")")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addChapter(title: String, description: String, videoUrl: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, title)
            put(COLUMN_DESCRIPTION, description)
            put(COLUMN_VIDEO_URL, videoUrl)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllChapters(): List<Chapter> {
        val chapterList = mutableListOf<Chapter>()
        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        cursor.use { cursor ->
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val chapter = Chapter(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        videoUrl = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VIDEO_URL))
                    )
                    chapterList.add(chapter)
                } while (cursor.moveToNext())
            }
        }
        db.close()
        return chapterList
    }
}
