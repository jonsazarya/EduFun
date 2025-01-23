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
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "mata_pelajaran"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAMA = "nama"
        private const val COLUMN_DESKRIPSI = "deskripsi"
        private const val COLUMN_IMAGE = "image"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAMA TEXT, " +
                "$COLUMN_DESKRIPSI TEXT, " +
                "$COLUMN_IMAGE INTEGER" +
                ")")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addCategory(nama: String, deskripsi: String, imageResId: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAMA, nama)
            put(COLUMN_DESKRIPSI, deskripsi)
            put(COLUMN_IMAGE, imageResId)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllCategory(): List<Category> {
        val mataPelajaranList = mutableListOf<Category>()
        val db = this.readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        cursor.use { cursor ->
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val mataPelajaran = Category(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESKRIPSI)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IMAGE))
                    )
                    mataPelajaranList.add(mataPelajaran)
                } while (cursor.moveToNext())
            }
        }
        db.close()
        return mataPelajaranList
    }
}
