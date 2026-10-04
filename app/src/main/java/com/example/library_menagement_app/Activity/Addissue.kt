package com.example.library_menagement_app.Activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.library_menagement_app.Databse.AppDatabase
import com.example.library_menagement_app.Databse.BookDao
import com.example.library_menagement_app.Databse.StudentDao
import com.example.library_menagement_app.Model.Book
import com.example.library_menagement_app.Model.Issue
import com.example.library_menagement_app.Model.Student
import com.example.library_menagement_app.databinding.ActivityAddIssueBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class Addissue : AppCompatActivity() {

    private lateinit var binding: ActivityAddIssueBinding
    private lateinit var database: AppDatabase
    private lateinit var bookDao: BookDao
    private lateinit var studentDao: StudentDao

    private var userId = 0

    private var bookList = ArrayList<Book>()
    private var studentList = ArrayList<Student>()

    private var selectedBookId = 0
    private var selectedStudentId = 0

    private var startDate = ""
    private var endDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddIssueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = getSharedPreferences("login", MODE_PRIVATE)
        userId = preferences.getInt("userId", 0)

        database = AppDatabase.getDatabase(this)

        bookDao = database.bookDao()
        studentDao = database.studentDao()

        binding.btnBack.setOnClickListener {
            finish()
        }

        loadBooks()
        loadStudents()

        binding.etStartDate.setOnClickListener {
            showStartDatePicker()
        }

        binding.etEndDate.setOnClickListener {
            showEndDatePicker()
        }

        binding.btnIssueBook.setOnClickListener {
            issueBook()
        }
    }

    private fun loadBooks() {

        lifecycleScope.launch(Dispatchers.IO) {

            val books = bookDao.getAllBooks(userId)

            runOnUiThread {

                bookList.clear()
                bookList.addAll(books)

                val bookNames = ArrayList<String>()

                bookNames.add("Select Book")

                for (book in bookList) {
                    bookNames.add(book.name)
                }

                val adapter = ArrayAdapter(
                    this@Addissue, android.R.layout.simple_spinner_item, bookNames
                )

                adapter.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item
                )

                binding.spinnerBook.adapter = adapter

                binding.spinnerBook.onItemSelectedListener =
                    object : android.widget.AdapterView.OnItemSelectedListener {

                        override fun onItemSelected(
                            parent: android.widget.AdapterView<*>?,
                            view: android.view.View?,
                            position: Int,
                            id: Long
                        ) {

                            if (position == 0) {

                                selectedBookId = 0

                            } else {

                                selectedBookId = bookList[position - 1].id
                            }
                        }

                        override fun onNothingSelected(
                            parent: android.widget.AdapterView<*>?
                        ) {
                        }
                    }
            }
        }
    }

    private fun loadStudents() {

        lifecycleScope.launch(Dispatchers.IO) {

            val students = studentDao.getAllStudents(userId)

            runOnUiThread {

                studentList.clear()
                studentList.addAll(students)

                val studentNames = ArrayList<String>()

                studentNames.add("Select Student")

                for (student in studentList) {
                    studentNames.add(student.name)
                }

                val adapter = ArrayAdapter(
                    this@Addissue, android.R.layout.simple_spinner_item, studentNames
                )

                adapter.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item
                )

                binding.spinnerStudent.adapter = adapter

                binding.spinnerStudent.onItemSelectedListener =
                    object : android.widget.AdapterView.OnItemSelectedListener {

                        override fun onItemSelected(
                            parent: android.widget.AdapterView<*>?,
                            view: android.view.View?,
                            position: Int,
                            id: Long
                        ) {

                            if (position == 0) {

                                selectedStudentId = 0

                            } else {

                                selectedStudentId =
                                    studentList[position - 1].id
                            }
                        }

                        override fun onNothingSelected(
                            parent: android.widget.AdapterView<*>?
                        ) {
                        }
                    }
            }
        }
    }

    private fun showStartDatePicker() {

        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->

                startDate = String.format(
                    "%04d-%02d-%02d",
                    selectedYear,
                    selectedMonth + 1,
                    selectedDay
                )

                binding.etStartDate.setText(startDate)
            },
            year,
            month,
            day
        )

        datePicker.show()
    }

    private fun showEndDatePicker() {

        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->

                endDate = String.format(
                    "%04d-%02d-%02d",
                    selectedYear,
                    selectedMonth + 1,
                    selectedDay
                )

                binding.etEndDate.setText(endDate)
            },
            year,
            month,
            day
        )

        datePicker.show()
    }

    private fun issueBook() {

        if (selectedBookId == 0) {

            Toast.makeText(this, "Select book", Toast.LENGTH_SHORT).show()

        } else if (selectedStudentId == 0) {

            Toast.makeText(this, "Select student", Toast.LENGTH_SHORT).show()

        } else if (startDate.isEmpty()) {

            Toast.makeText(this, "Select start date", Toast.LENGTH_SHORT).show()

        } else if (endDate.isEmpty()) {

            Toast.makeText(this, "Select end date", Toast.LENGTH_SHORT).show()

        } else if (endDate < startDate) {

            Toast.makeText(this, "End date must be after start date", Toast.LENGTH_SHORT).show()

        } else {

            var book: Book? = null

            for (item in bookList) {

                if (item.id == selectedBookId) {
                    book = item
                }
            }

            var student: Student? = null

            for (item in studentList) {

                if (item.id == selectedStudentId) {
                    student = item
                }
            }

            if (book != null && student != null) {

                if (book.quantity <= 0) {

                    Toast.makeText(this, "Book is not available right now", Toast.LENGTH_SHORT).show()

                    return
                }

                val issue = Issue(
                    bookId = book.id,
                    studentId = student.id,
                    bookName = book.name,
                    studentName = student.name,
                    startDate = startDate,
                    endDate = endDate,
                    userId = userId
                )

                lifecycleScope.launch(Dispatchers.IO) {

                    database.issueDao().insertIssue(issue)

                    bookDao.decreaseQuantity(book.id)

                    runOnUiThread {

                        Toast.makeText(this@Addissue, "Book issued successfully", Toast.LENGTH_SHORT).show()

                        finish()
                    }
                }
            }
        }
    }
}