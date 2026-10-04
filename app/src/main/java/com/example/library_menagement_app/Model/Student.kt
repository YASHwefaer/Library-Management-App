package com.example.library_menagement_app.Model

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "students")
data class Student(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,

    val email: String,

    val phone: String,

    var userId : Int
)