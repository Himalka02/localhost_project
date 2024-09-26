package com.example.localhost_project

import android.database.Cursor
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    
    lateinit var dbHelper:databasehelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        dbHelper = databasehelper(this)

        val studentIdEditText = findViewById<EditText>(R.id.studentIdEditText)
        val studentNameEditText = findViewById<EditText>(R.id.studentNameEditText)
        val studentMarksEditText = findViewById<EditText>(R.id.studentMarksEditText)
        val saveButton = findViewById<Button>(R.id.saveButton)
        val searchButton = findViewById<Button>(R.id.searchButton)
        val resultTextView = findViewById<TextView>(R.id.resultTextView)

        // Handle save button click
        saveButton.setOnClickListener {
            val id = studentIdEditText.text.toString().toIntOrNull()
            val name = studentNameEditText.text.toString()
            val marks = studentMarksEditText.text.toString().toIntOrNull()

            // Validate the input
            if (id != null && name.isNotEmpty() && marks != null) {
                // Save the student data
                val success = dbHelper.addStudent(id, name, marks)
                if (success) {
                    Toast.makeText(this, "Student saved", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error saving student", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle search button click
        searchButton.setOnClickListener {
            val id = studentIdEditText.text.toString().toIntOrNull()
            if (id != null) {
                val cursor: Cursor? = dbHelper.getStudentById(id)
                if (cursor != null && cursor.moveToFirst()) {
                    val studentName = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                    val studentMarks = cursor.getInt(cursor.getColumnIndexOrThrow("marks"))
                    resultTextView.text = "ID: $id\nName: $studentName\nMarks: $studentMarks"
                    cursor.close()
                } else {
                    resultTextView.text = "Student not found"
                }
            } else {
                Toast.makeText(this, "Please enter a valid ID", Toast.LENGTH_SHORT).show()
            }
        }

    }
}