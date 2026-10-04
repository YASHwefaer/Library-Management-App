package com.example.library_menagement_app.Activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.library_menagement_app.Databse.AppDatabase
import com.example.library_menagement_app.Databse.StudentDao
import com.example.library_menagement_app.Model.Student
import com.example.library_menagement_app.databinding.ActivityAddStudentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddStudent : AppCompatActivity() {

    private lateinit var binding: ActivityAddStudentBinding
    private lateinit var database: AppDatabase
    private lateinit var studentDao: StudentDao

    private var studentId = 0
    private var userId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = getSharedPreferences("login", MODE_PRIVATE)

        userId = preferences.getInt("userId", 0)

        database = AppDatabase.getDatabase(this)
        studentDao = database.studentDao()

        studentId = intent.getIntExtra("id", 0)

        if (studentId != 0) {

            binding.Tvtitle.text = "Edit Student"

            binding.btnAddStudent.text = "Update Student"

            binding.etStudentName.setText(
                intent.getStringExtra("name")
            )

            binding.etStudentEmail.setText(
                intent.getStringExtra("email")
            )

            binding.etStudentPhone.setText(
                intent.getStringExtra("phone")
            )
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnAddStudent.setOnClickListener {
            saveStudent()
        }
    }

    private fun saveStudent() {

        val name = binding.etStudentName.text.toString()
        val email = binding.etStudentEmail.text.toString()
        val phone = binding.etStudentPhone.text.toString()

        if (name.isEmpty()) {

            binding.etStudentName.error = "Enter student name"

        } else if (email.isEmpty()) {

            binding.etStudentEmail.error = "Enter email"

        } else if (phone.isEmpty()) {

            binding.etStudentPhone.error = "Enter phone number"

        } else {

            val student = Student(
                id = studentId,
                name = name,
                email = email,
                phone = phone,
                userId = userId
            )

            lifecycleScope.launch(Dispatchers.IO) {

                if (studentId == 0) {

                    studentDao.insertStudent(student)

                } else {

                    studentDao.updateStudent(student)
                }

                runOnUiThread {

                    if (studentId == 0) {

                        Toast.makeText(this@AddStudent, "Student added successfully", Toast.LENGTH_SHORT).show()

                    } else {

                        Toast.makeText(this@AddStudent, "Student updated successfully", Toast.LENGTH_SHORT).show()
                    }

                    finish()
                }
            }
        }
    }
}