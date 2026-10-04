package com.example.library_menagement_app.Activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.library_menagement_app.Adapter.IssueAdapter
import com.example.library_menagement_app.Databse.AppDatabase
import com.example.library_menagement_app.Databse.BookDao
import com.example.library_menagement_app.Databse.IssueDao
import com.example.library_menagement_app.Model.Issue
import com.example.library_menagement_app.databinding.ActivityViewissueBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Viewissue : AppCompatActivity() {

    private lateinit var binding: ActivityViewissueBinding
    private lateinit var database: AppDatabase
    private lateinit var issueDao: IssueDao
    private lateinit var bookDao: BookDao
    private lateinit var adapter: IssueAdapter

    private var userId = 0

    private val issueList = ArrayList<Issue>()
    private val allIssueList = ArrayList<Issue>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityViewissueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = getSharedPreferences("login", MODE_PRIVATE)

        userId = preferences.getInt("userId", 0)

        database = AppDatabase.getDatabase(this)

        issueDao = database.issueDao()
        bookDao = database.bookDao()

        binding.RecyclerIssue.layoutManager = LinearLayoutManager(this)

        adapter = IssueAdapter(issueList, this)

        binding.RecyclerIssue.adapter = adapter

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.BtnAddisuue.setOnClickListener {

            val intent = Intent(this@Viewissue, Addissue::class.java)
            startActivity(intent)
        }

        binding.etSearchIssue.addTextChangedListener(
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
                    searchIssue(s.toString())
                }

                override fun afterTextChanged(
                    s: Editable?
                ) {
                }
            }
        )

        loadIssues()
    }

    private fun loadIssues() {

        lifecycleScope.launch(Dispatchers.IO) {

            val issues = issueDao.getAllIssues(userId)

            runOnUiThread {

                allIssueList.clear()
                allIssueList.addAll(issues)

                issueList.clear()
                issueList.addAll(issues)

                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun searchIssue(searchText: String) {

        val search = searchText.trim()

        if (search.isEmpty()) {

            issueList.clear()
            issueList.addAll(allIssueList)

            adapter.notifyDataSetChanged()

            return
        }

        val searchList = ArrayList<Issue>()

        for (issue in allIssueList) {

            if (
                issue.bookName.contains(search, true) ||
                issue.studentName.contains(search, true) ||
                issue.startDate.contains(search, true) ||
                issue.endDate.contains(search, true)
            ) {

                searchList.add(issue)
            }
        }

        issueList.clear()
        issueList.addAll(searchList)

        adapter.notifyDataSetChanged()

        if (searchList.isEmpty()) {

            Toast.makeText(this, "No issue found", Toast.LENGTH_SHORT).show()
        }
    }

    fun returnBook(issue: Issue) {

        val returnBook = com.example.library_menagement_app.Model.ReturnBook(
            bookId = issue.bookId,
            studentId = issue.studentId,
            bookName = issue.bookName,
            studentName = issue.studentName,
            startDate = issue.startDate,
            endDate = issue.endDate,
            returnDate = java.text.SimpleDateFormat(
                "yyyy-MM-dd",
                java.util.Locale.getDefault()
            ).format(java.util.Date()),
            userId = userId
        )

        lifecycleScope.launch(Dispatchers.IO) {

            database.returnBookDao().insertReturnBook(returnBook)

            bookDao.increaseQuantity(issue.bookId)

            issueDao.deleteIssue(issue)

            runOnUiThread {

                Toast.makeText(this@Viewissue, "Book returned successfully", Toast.LENGTH_SHORT).show()

                loadIssues()
            }
        }
    }

    override fun onResume() {

        super.onResume()

        if (::issueDao.isInitialized) {
            loadIssues()
        }
    }
}