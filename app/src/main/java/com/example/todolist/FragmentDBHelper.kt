package com.example.todolist

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class FragmentDBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    // below is the method for creating a database by a sqlite query
    override fun onCreate(db: SQLiteDatabase) {
        // below is a sqlite query, where column names
        // along with their data types is given
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                TEXT_COL + " TEXT" + ")")

        // we are calling sqlite
        // method for executing our query
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // this method is to check if table already exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    // We only care about the item with ID 1
    fun addItem(name : String){
        val db = this.writableDatabase

        // Check if the table is empty
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_NAME", null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()

        // If the table is empty, insert the item
        if (count == 0) {
            val values = ContentValues()
            values.put(ID_COL, 1)
            values.put(TEXT_COL, name)
            db.insert(TABLE_NAME, null, values)
        } else {
            updateItem(1, name)
        }

        db.close()
    }

    // below method is to get
    // all data from our database
    fun getAllItems(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null)
    }

    fun getSingleItemText(): String {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null)
        val textColIndex = cursor.getColumnIndex(TEXT_COL)
        var text = ""
        if (textColIndex != -1 && cursor.moveToFirst())
            text = cursor.getString(textColIndex)
        cursor.close()
        return text
    }

    fun loadAndParseItemsOnInit() : String {
        var returnText = "not placeholder text"
        val db = this.readableDatabase

        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val idColIndex = cursor.getColumnIndex(ID_COL)
        val textColIndex = cursor.getColumnIndex(TEXT_COL)
        if (idColIndex != -1 && textColIndex != -1 && cursor.moveToFirst() ) {
            do {
                val id = cursor.getInt(idColIndex)
                val fragmentText = cursor.getString(textColIndex)
                if (id == 1) {
                    returnText = fragmentText.toString()
                    break
                }
            } while (cursor.moveToNext())
        } else {
            addItem(returnText)
        }
        cursor.close()

        return returnText
    }

    fun updateItem(id: Int, newText: String) {
        val values = ContentValues()
        values.put(TEXT_COL, newText)
        val db = this.writableDatabase
        val selection = "$ID_COL = ?"
        db.update(TABLE_NAME, values, selection, arrayOf(id.toString()))
        db.close()
    }

    fun deleteItem(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$ID_COL = ?", arrayOf(id.toString()))
        db.close()
    }

    fun getNextAvailableID(): Int {
        val db = this.readableDatabase
        var nextId = 1 // Default next ID

        val cursor = db.rawQuery("SELECT MAX(id) FROM $TABLE_NAME", null)
        cursor.moveToFirst()
        val maxId = cursor.getInt(0)
        if (!cursor.isNull(0)) {
            nextId = maxId + 1
        }
        cursor.close()

        return nextId
    }

    companion object{
        // here we have defined variables for our database

        val DATABASE_NAME = "FRAGMENT_DB"

        private val DATABASE_VERSION = 1

        val TABLE_NAME = "fragment_table"

        val ID_COL = "id"

        val TEXT_COL = "text"

    }
}