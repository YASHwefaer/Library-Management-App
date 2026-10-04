package com.example.library_menagement_app.Databse

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import com.example.library_menagement_app.Model.Issue

@Dao
interface IssueDao {

    @Insert
    suspend fun insertIssue(issue: Issue)

    @Query("SELECT * FROM issues WHERE userId = :userId ORDER BY id DESC")
    suspend fun getAllIssues(userId: Int): List<Issue>

    @Delete
    suspend fun deleteIssue(issue: Issue)
}