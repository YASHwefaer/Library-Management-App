package com.example.library_menagement_app.Databse

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import com.example.library_menagement_app.Model.User

@Dao
interface UserDao {

    @Insert
    fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    fun loginUser(email: String, password: String): User?
}