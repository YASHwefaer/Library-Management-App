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
import com.example.library_menagement_app.Adapter.BookAdapter
import com.example.library_menagement_app.Databse.AppDatabase
import com.example.library_menagement_app.Databse.BookDao
import com.example.library_menagement_app.Model.Book
import com.example.library_menagement_app.databinding.ActivityBookBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookActivity : AppCompatActivity(), BookAdapter.OnBookClick {

    private lateinit var binding: ActivityBookBinding
    private lateinit var database: AppDatabase
    private lateinit var bookDao: BookDao
    private lateinit var adapter: BookAdapter

    private var userId = 0

    private val bookList = ArrayList<Book>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = getSharedPreferences("login", MODE_PRIVATE)

        userId = preferences.getInt("userId", 0)

        database = AppDatabase.getDatabase(this)
        bookDao = database.bookDao()

        binding.recyclerBooks.layoutManager = LinearLayoutManager(this)

        adapter = BookAdapter(bookList, this)

        binding.recyclerBooks.adapter = adapter

        binding.btnback.setOnClickListener {
            finish()
        }

        binding.btnAddBook.setOnClickListener {

            val intent = Intent(this, Addbook::class.java)

            startActivity(intent)
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {

            }

            override fun onTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {
                searchBooks(p0.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        loadBooks()
    }

    private fun loadBooks() {

        lifecycleScope.launch(Dispatchers.IO) {

            val books = bookDao.getAllBooks(userId)

            runOnUiThread {

                bookList.clear()

                bookList.addAll(books)

                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun searchBooks(searchText: String) {

        lifecycleScope.launch(Dispatchers.IO) {

            val books = bookDao.getAllBooks(userId)

            val searchList = ArrayList<Book>()

            for (book in books) {

                if (
                    book.name.contains(searchText, true) ||
                    book.author.contains(searchText, true) ||
                    book.category.contains(searchText, true) ||
                    book.isbn.contains(searchText, true)
                ) {

                    searchList.add(book)
                }
            }

            runOnUiThread {

                bookList.clear()

                bookList.addAll(searchList)

                adapter.notifyDataSetChanged()

                if (searchList.isEmpty()) {

                    Toast.makeText(
                        this@BookActivity,
                        "No books found",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun editBook(book: Book) {

        val intent = Intent(this, Addbook::class.java)

        intent.putExtra("id", book.id)
        intent.putExtra("name", book.name)
        intent.putExtra("author", book.author)
        intent.putExtra("isbn", book.isbn)
        intent.putExtra("category", book.category)
        intent.putExtra("quantity", book.quantity)
        intent.putExtra("image", book.image)

        startActivity(intent)
    }

    override fun deleteBook(book: Book) {

        val builder = AlertDialog.Builder(this)

        builder.setTitle("Delete Book")

        builder.setMessage(
            "Are you sure you want to delete this book?"
        )

        builder.setPositiveButton(
            "Yes",
            object : DialogInterface.OnClickListener {

                override fun onClick(
                    dialog: DialogInterface,
                    which: Int
                ) {

                    lifecycleScope.launch(Dispatchers.IO) {

                        bookDao.deleteBook(book)

                        runOnUiThread {

                            Toast.makeText(this@BookActivity, "Book deleted", Toast.LENGTH_SHORT).show()

                            loadBooks()
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

    override fun onResume() {

        super.onResume()

        if (::bookDao.isInitialized) {

            loadBooks()
        }
    }
}