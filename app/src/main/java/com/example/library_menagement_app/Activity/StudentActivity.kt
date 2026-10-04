package com.example.library_menagement_app.Activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.library_menagement_app.Adapter.StudentAdapter
import com.example.library_menagement_app.Databse.AppDatabase
import com.example.library_menagement_app.Databse.StudentDao
import com.example.library_menagement_app.Model.Student
import com.example.library_menagement_app.databinding.ActivityStudentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StudentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentBinding
    private lateinit var database: AppDatabase
    private lateinit var studentDao: StudentDao
    private lateinit var adapter: StudentAdapter

    private var userId = 0

    private val studentList = ArrayList<Student>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = getSharedPreferences("login", MODE_PRIVATE)

        userId = preferences.getInt("userId", 0)

        database = AppDatabase.getDatabase(this)
        studentDao = database.studentDao()

        binding.recyclerStudents.layoutManager = LinearLayoutManager(this)

        adapter = StudentAdapter(studentList, this)

        binding.recyclerStudents.adapter = adapter

        binding.btnback.setOnClickListener {
            finish()
        }

        binding.BtnAddStudent.setOnClickListener {
            val intent = Intent(this, AddStudent::class.java)
            startActivity(intent)
        }

        binding.etSearchStudent.addTextChangedListener(
            object : TextWatcher {

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    searchStudent(s.toString())
                }

                override fun afterTextChanged(
                    s: Editable?
                ) {
                }
            }
        )

        loadStudents()
    }

    fun editStudent(student: Student) {

        val intent = Intent(this, AddStudent::class.java)

        intent.putExtra("id", student.id)
        intent.putExtra("name", student.name)
        intent.putExtra("email", student.email)
        intent.putExtra("phone", student.phone)

        startActivity(intent)
    }

    fun deleteStudent(student: Student) {

        val builder = AlertDialog.Builder(this)

        builder.setTitle("Delete Student")

        builder.setMessage(
            "Are you sure you want to delete this student?"
        )



        builder.setPositiveButton(
            "Yes",
            object : DialogInterface.OnClickListener {

                override fun onClick(
                    dialog: DialogInterface,
                    which: Int
                ) {

                    lifecycleScope.launch(Dispatchers.IO) {

                        studentDao.deleteStudent(student)

                        runOnUiThread {

                            Toast.makeText(this@StudentActivity, "Student deleted", Toast.LENGTH_SHORT).show()

                            loadStudents()
                        }
                    }
                }
            }
        )

        builder.setNegativeButton(
            "No",
            null
        )

        builder.show()
   }

    private fun searchStudent(search: String) {

        lifecycleScope.launch(Dispatchers.IO) {

            val students: List<Student>

            if (search.isEmpty()) {

                students = studentDao.getAllStudents(userId)

            } else {

                students = studentDao.searchStudent(search, userId)
            }

            runOnUiThread {

                studentList.clear()
                studentList.addAll(students)

                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun loadStudents() {

        lifecycleScope.launch(Dispatchers.IO) {

            val students = studentDao.getAllStudents(userId)

            runOnUiThread {

                studentList.clear()
                studentList.addAll(students)

                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onResume() {

        super.onResume()

        if (::studentDao.isInitialized) {
            loadStudents()
        }
    }
}