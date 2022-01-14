package com.database.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.database.databinding.ActivityMainBinding
import com.database.db.DatabaseHelper
import com.database.model.Person

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var databaseHelper: DatabaseHelper
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper.getInstance(this)

        /*databaseHelper.insertData(Person(name = "Sateesh", age = 27, mobile = 8618378828))
        databaseHelper.insertData(Person(name = "Ramesh", age = 30, mobile = 9686025367))*/

        databaseHelper.getPersonData().forEach {
            Log.i(TAG, it.toString())
        }

    }
}