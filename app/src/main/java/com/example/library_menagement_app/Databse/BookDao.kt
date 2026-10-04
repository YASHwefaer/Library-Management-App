package com.example.library_menagement_app.Databse

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.library_menagement_app.Model.Book

@Dao
interface BookDao {

    @Insert
    suspend fun insertBook(book: Book)

    @Update
    suspend fun updateBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)

    @Query("SELECT * FROM books WHERE userId = :userId")
    suspend fun getAllBooks(userId: Int): List<Book>

    @Query("UPDATE books SET quantity = quantity - 1 WHERE id = :bookId AND quantity > 0")
    suspend fun decreaseQuantity(bookId: Int)

    @Query("UPDATE books SET quantity = quantity + 1 WHERE id = :bookId")
    suspend fun increaseQuantity(bookId: Int)
}