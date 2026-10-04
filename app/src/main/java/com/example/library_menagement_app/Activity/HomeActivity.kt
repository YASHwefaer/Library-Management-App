package com.example.library_menagement_app.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.library_menagement_app.Databse.AppDatabase
import com.example.library_menagement_app.Databse.BookDao
import com.example.library_menagement_app.Databse.IssueDao
import com.example.library_menagement_app.Databse.StudentDao
import com.example.library_menagement_app.databinding.ActivityHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var database: AppDatabase
    private lateinit var bookDao: BookDao
    private lateinit var studentDao: StudentDao
    private lateinit var issueDao: IssueDao

    private var userId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)

        val name = sharedPreferences.getString("name", "")

        userId = sharedPreferences.getInt("userId", 0)

        binding.tvEmail.text = name
        binding.tvname.text = name

        database = AppDatabase.getDatabase(this)

        bookDao = database.bookDao()
        studentDao = database.studentDao()
        issueDao = database.issueDao()

        loadDashboardData()

        binding.btnMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(binding.navigationDrawer)
        }

        binding.btnDashboard.setOnClickListener {
            binding.drawerLayout.closeDrawer(binding.navigationDrawer)
        }

        binding.btnDrawerBooks.setOnClickListener {

            val intent = Intent(this, BookActivity::class.java)

            startActivity(intent)

            binding.drawerLayout.closeDrawer(
                binding.navigationDrawer
            )
        }

        binding.btnDrawerStudents.setOnClickListener {

            val intent = Intent(this, StudentActivity::class.java)

            startActivity(intent)

            binding.drawerLayout.closeDrawer(
                binding.navigationDrawer
            )
        }

        binding.btnReturnbook.setOnClickListener {

            val intent = Intent(this, ReturnBookActivity::class.java)

            startActivity(intent)

            binding.drawerLayout.closeDrawer(binding.navigationDrawer)
        }

        binding.btnDrawerIssueBook.setOnClickListener {

            val intent = Intent(this, Addissue::class.java)

            startActivity(intent)

            binding.drawerLayout.closeDrawer(
                binding.navigationDrawer
            )
        }

        binding.btnViewIssue.setOnClickListener {

            val intent = Intent(this, Viewissue::class.java)

            startActivity(intent)

            binding.drawerLayout.closeDrawer(
                binding.navigationDrawer
            )
        }

        binding.btnAddBook.setOnClickListener {

            val intent = Intent(this, BookActivity::class.java)

            startActivity(intent)
        }

        binding.btnIssueBook.setOnClickListener {

            val intent = Intent(this, Viewissue::class.java)

            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {

            sharedPreferences.edit()
                .putBoolean("islogin", false)
                .apply()

            val intent = Intent(this, LoginPage::class.java)

            startActivity(intent)

            finish()
        }
    }

    private fun loadDashboardData() {

        lifecycleScope.launch(Dispatchers.IO) {

            val books = bookDao.getAllBooks(userId)

            val students = studentDao.getAllStudents(userId)

            val issues = issueDao.getAllIssues(userId)

            val totalBooks = books.size

            val issuedBooks = issues.size

            var availableBooks = 0

            for (book in books) {

                if (book.quantity > 0) {
                    availableBooks++
                }
            }

            val totalStudents = students.size

            runOnUiThread {

                binding.tvTotalBooks.text = totalBooks.toString()

                binding.tvAvailableBooks.text = availableBooks.toString()

                binding.tvIssuedBooks.text = issuedBooks.toString()

                binding.tvTotalStudents.text = totalStudents.toString()
            }
        }
    }

    override fun onResume() {

        super.onResume()

        if (::bookDao.isInitialized) {
            loadDashboardData()
        }
    }
}