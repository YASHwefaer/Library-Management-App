package com.example.library_menagement_app.Model

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "books")
data class Book(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val author: String,
    val category: String,
    val isbn: String,
    val quantity: Int,
    val image: String,
    val userEmail: String,
    var userId : Int
)