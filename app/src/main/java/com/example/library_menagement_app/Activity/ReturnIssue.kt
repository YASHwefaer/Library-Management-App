package com.example.library_menagement_app.Activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.library_menagement_app.Adapter.ReturnBookAdapter
import com.example.library_menagement_app.Databse.AppDatabase
import com.example.library_menagement_app.Databse.ReturnBookDao
import com.example.library_menagement_app.Model.ReturnBook
import com.example.library_menagement_app.databinding.ActivityReturnIssueBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReturnBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReturnIssueBinding
    private lateinit var database: AppDatabase
    private lateinit var returnBookDao: ReturnBookDao
    private lateinit var adapter: ReturnBookAdapter

    private var userId = 0

    private val returnBookList = ArrayList<ReturnBook>()
    private val allReturnBookList = ArrayList<ReturnBook>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReturnIssueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = getSharedPreferences("login", MODE_PRIVATE)

        userId = preferences.getInt("userId", 0)

        database = AppDatabase.getDatabase(this)

        returnBookDao = database.returnBookDao()

        binding.recyclerReturnBooks.layoutManager =
            LinearLayoutManager(this)

        adapter = ReturnBookAdapter(returnBookList)

        binding.recyclerReturnBooks.adapter = adapter

        binding.btnBack.setOnClickListener {
            finish()
        }


        binding.etSearchReturn.addTextChangedListener(
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
                    searchReturnBooks(s.toString())
                }

                override fun afterTextChanged(
                    s: Editable?
                ) {
                }
            }
        )

        loadReturnBooks()
    }

    private fun loadReturnBooks() {

        lifecycleScope.launch(Dispatchers.IO) {

            val returnBooks = returnBookDao.getAllReturnBooks(userId)

            runOnUiThread {

                allReturnBookList.clear()
                allReturnBookList.addAll(returnBooks)

                returnBookList.clear()
                returnBookList.addAll(returnBooks)

                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun searchReturnBooks(searchText: String) {

        val search = searchText.trim()

        if (search.isEmpty()) {

            returnBookList.clear()
            returnBookList.addAll(allReturnBookList)

            adapter.notifyDataSetChanged()

            return
        }

        val searchList = ArrayList<ReturnBook>()

        for (returnBook in allReturnBookList) {

            if (
                returnBook.bookName.contains(search, true) ||
                returnBook.studentName.contains(search, true) ||
                returnBook.startDate.contains(search, true) ||
                returnBook.endDate.contains(search, true) ||
                returnBook.returnDate.contains(search, true)
            ) {
                searchList.add(returnBook)
            }
        }

        returnBookList.clear()
        returnBookList.addAll(searchList)

        adapter.notifyDataSetChanged()

        if (searchList.isEmpty()) {

            Toast.makeText(this, "No return record found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {

        super.onResume()

        if (::returnBookDao.isInitialized) {
            loadReturnBooks()
        }
    }
}