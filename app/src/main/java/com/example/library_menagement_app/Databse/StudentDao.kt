package com.example.library_menagement_app.Databse

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.library_menagement_app.Model.Student

@Dao
interface StudentDao {

    @Insert
    fun insertStudent(student: Student)

    @Query("SELECT * FROM students WHERE userId = :userId")
    fun getAllStudents(userId: Int): List<Student>

    @Update
    fun updateStudent(student: Student)

    @Delete
    fun deleteStudent(student: Student)

    @Query("SELECT * FROM students WHERE name LIKE '%' || :search || '%' AND userId = :userId")
    fun searchStudent(search: String, userId: Int): List<Student>

    @Query("SELECT COUNT(*) FROM students WHERE userId = :userId")
    fun getStudentCount(userId: Int): Int
}