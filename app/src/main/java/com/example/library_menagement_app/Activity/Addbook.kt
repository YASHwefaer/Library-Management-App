package com.example.library_menagement_app.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.library_menagement_app.Databse.AppDatabase
import com.example.library_menagement_app.Databse.BookDao
import com.example.library_menagement_app.Model.Book
import com.example.library_menagement_app.databinding.ActivityAddbookBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class Addbook : AppCompatActivity() {

    private lateinit var binding: ActivityAddbookBinding
    private lateinit var database: AppDatabase
    private lateinit var bookDao: BookDao

    private var email = ""
    private var userId = 0
    private var imagePath = ""
    private var bookId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddbookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)
        bookDao = database.bookDao()

        bookId = intent.getIntExtra("id", 0)

        val preferences = getSharedPreferences("login", MODE_PRIVATE)

        email = preferences.getString("email", "") ?: ""
        userId = preferences.getInt("userId", 0)

        if (bookId != 0) {

            binding.Tvtitle.text = "Edit Book"

            binding.etBookName.setText(intent.getStringExtra("name"))
            binding.etAuthor.setText(intent.getStringExtra("author"))
            binding.etIsbn.setText(intent.getStringExtra("isbn"))
            binding.etCategory.setText(intent.getStringExtra("category"))
            binding.etQuantity.setText(
                intent.getIntExtra("quantity", 0).toString()
            )

            imagePath = intent.getStringExtra("image").toString()

            if (imagePath.isNotEmpty()) {
                binding.imgBook.setImageURI(Uri.parse(imagePath))
            }

            binding.btnSaveBook.text = "Update Book"
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnSelectImage.setOnClickListener {

            val intent = Intent(Intent.ACTION_PICK)

            intent.type = "image/*"

            startActivityForResult(intent, 100)
        }

        binding.btnSaveBook.setOnClickListener {
            saveBook()
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {

            val image = data.data

            if (image != null) {

                binding.imgBook.setImageURI(image)

                imagePath = saveImage(image)
            }
        }
    }

    private fun saveImage(uri: Uri): String {

        val fileName = "book_" + System.currentTimeMillis() + ".jpg"

        val file = File(filesDir, fileName)

        val inputStream = contentResolver.openInputStream(uri)

        val outputStream = file.outputStream()

        if (inputStream != null) {

            val buffer = ByteArray(1024)

            var length = inputStream.read(buffer)

            while (length > 0) {

                outputStream.write(buffer, 0, length)

                length = inputStream.read(buffer)
            }

            inputStream.close()
        }

        outputStream.close()

        return file.absolutePath
    }

    private fun saveBook() {

        val name = binding.etBookName.text.toString()
        val author = binding.etAuthor.text.toString()
        val isbn = binding.etIsbn.text.toString()
        val category = binding.etCategory.text.toString()
        val quantityText = binding.etQuantity.text.toString()

        if (imagePath.isEmpty()) {

            Toast.makeText(
                this,
                "Select image",
                Toast.LENGTH_SHORT
            ).show()

        } else if (name.isEmpty()) {

            binding.etBookName.error = "Enter book name"

        } else if (author.isEmpty()) {

            binding.etAuthor.error = "Enter author"

        } else if (isbn.isEmpty()) {

            binding.etIsbn.error = "Enter ISBN"

        } else if (category.isEmpty()) {

            binding.etCategory.error = "Enter category"

        } else if (quantityText.isEmpty()) {

            binding.etQuantity.error = "Enter quantity"

        } else {

            val quantity = quantityText.toInt()

            val book = Book(
                id = bookId,
                name = name,
                author = author,
                category = category,
                isbn = isbn,
                quantity = quantity,
                image = imagePath,
                userEmail = email,
                userId = userId
            )

            lifecycleScope.launch(Dispatchers.IO) {

                if (bookId == 0) {

                    bookDao.insertBook(book)

                } else {

                    bookDao.updateBook(book)
                }

                runOnUiThread {

                    if (bookId == 0) {

                        Toast.makeText(
                            this@Addbook,
                            "Book added successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        Toast.makeText(
                            this@Addbook,
                            "Book updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    finish()
                }
            }
        }
    }
}