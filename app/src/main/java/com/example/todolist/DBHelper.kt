package com.example.todolist

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    // below is the method for creating a database by a sqlite query
    override fun onCreate(db: SQLiteDatabase) {
        // below is a sqlite query, where column names
        // along with their data types is given
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                NAME_COL + " TEXT," +
                CHECKED_COL + " BOOLEAN" + ")")

        // we are calling sqlite
        // method for executing our query
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // this method is to check if table already exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    // This method is for adding data in our database
    fun addItem(name : String, isChecked : Boolean ){
        val values = ContentValues()
        values.put(NAME_COL, name)
        values.put(CHECKED_COL, isChecked)

        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    // below method is to get
    // all data from our database
    fun getAllItems(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null)

    }

    fun loadAndParseItemsOnInit() : MutableList<TodoItem> {
        val todoList = mutableListOf<TodoItem>()
        val db = this.readableDatabase

        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val idColIndex = cursor.getColumnIndex(ID_COL)
        val nameColIndex = cursor.getColumnIndex(NAME_COL)
        val checkedColIndex = cursor.getColumnIndex(CHECKED_COL)
        if (idColIndex != -1 && nameColIndex != -1 && checkedColIndex != -1 && cursor.moveToFirst() ) {
            do {
                val id = cursor.getInt(idColIndex)
                val title = cursor.getString(nameColIndex)
                val isChecked = cursor.getInt(checkedColIndex) == 1

                val todo = TodoItem(id, title, isChecked)
                todoList.add(todo)
            } while (cursor.moveToNext())
        }
        cursor.close()

        return todoList
    }

    fun updateItemChecked(id: Int, isChecked: Boolean) {
        val values = ContentValues()
        values.put(CHECKED_COL, isChecked)
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

    fun deleteAllDoneItems() {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$CHECKED_COL = ?", arrayOf(1.toString()))
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

        // below is variable for database name
        private val DATABASE_NAME = "TODO_DB"

        // below is the variable for database version
        private val DATABASE_VERSION = 1

        // below is the variable for table name
        val TABLE_NAME = "todo_table"

        // below is the variable for id column
        val ID_COL = "id"

        // below is the variable for name column
        val NAME_COL = "task_name"

        // below is the variable for age column
        val CHECKED_COL = "checked"
    }
}