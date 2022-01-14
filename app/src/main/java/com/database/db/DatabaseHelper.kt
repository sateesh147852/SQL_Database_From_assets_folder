package com.database.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.database.model.Person
import com.database.utils.Constants
import com.database.utils.Constants.DB_NAME
import com.database.utils.Constants.TABLE_NAME
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class DatabaseHelper(private val context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, Constants.DB_VERSION) {

    private var DATABASE_PATH: String = ""
    private val TAG = "DatabaseHelper"

    init {
        openDataBase()
    }

    override fun onCreate(database: SQLiteDatabase) {

    }

    override fun onUpgrade(database: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    companion object {

        private var databaseHelper: DatabaseHelper? = null

        fun getInstance(context: Context): DatabaseHelper {
            if (databaseHelper == null) {
                synchronized(this) {
                    databaseHelper = DatabaseHelper(context)
                }
            }
            return databaseHelper!!
        }

    }

    private fun copyDatabase(file: File) {
        val inputStream: InputStream = context.assets.open(DB_NAME)
        val os: OutputStream = FileOutputStream(file)

        val buffer = ByteArray(1024)
        while (inputStream.read(buffer) > 0) {
            os.write(buffer)
        }

        os.flush()
        os.close()
        inputStream.close()
    }

    private fun openDataBase() {
        try {
            val file = context.getDatabasePath(DB_NAME)
            if (!file.exists()) {
                copyDatabase(file)
            }
        } catch (e: SQLiteException) {
            e.message
        }
    }

    fun deleteDatabase() {
        val databasePath = DATABASE_PATH + DB_NAME
        try {
            val file = File(databasePath)
            file.delete()
        } catch (e: Exception) {
            e.message
        }
    }

    fun insertData(person: Person) {
        val database = writableDatabase
        val contentValues: ContentValues = ContentValues()
        contentValues.put("name", person.name)
        contentValues.put("age", person.age)
        contentValues.put("Mobile", person.mobile)
        database.insert(TABLE_NAME, null, contentValues)
        database.close()
    }

    fun getPersonData(): List<Person> {
        val database = writableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val data = ArrayList<Person>()
        var cursor: Cursor? = null
        cursor = database.rawQuery(query, null)
        if (cursor != null) {
            var id: Int
            var name: String
            var age: Int
            var mobile: Long
            while (cursor.moveToNext()) {
                id = cursor.getInt(0)
                name = cursor.getString(1)
                age = cursor.getInt(2)
                mobile = cursor.getLong(3)
                data.add(Person(id, name, age, mobile))
            }
        }
        return data
    }
}