package com.example.library_menagement_app.Databse

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import com.example.library_menagement_app.Model.ReturnBook

@Dao
interface ReturnBookDao {

    @Insert
    suspend fun insertReturnBook(returnBook: ReturnBook)

    @Query("SELECT * FROM return_books WHERE userId = :userId ORDER BY id DESC")
    suspend fun getAllReturnBooks(userId: Int): List<ReturnBook>
}