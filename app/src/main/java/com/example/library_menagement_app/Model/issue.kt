package com.example.library_menagement_app.Model

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "issues")
data class Issue(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val bookId: Int,
    val studentId: Int,

    val bookName: String,
    val studentName: String,

    val startDate: String,
    val endDate: String,

    var userId : Int
)