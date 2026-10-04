package com.example.library_menagement_app.Databse

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.example.library_menagement_app.Model.Book
import com.example.library_menagement_app.Model.Issue
import com.example.library_menagement_app.Model.ReturnBook
import com.example.library_menagement_app.Model.Student
import com.example.library_menagement_app.Model.User

@Database(
    entities = [
        User::class,
        Book::class,
        Student::class,
        Issue::class,
        ReturnBook::class
    ],
    version = 6
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun bookDao(): BookDao
    abstract fun studentDao(): StudentDao
    abstract fun issueDao(): IssueDao
    abstract fun returnBookDao(): ReturnBookDao

    companion object {
        private var database: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            if (database == null) {

                database = Room.databaseBuilder(context, AppDatabase::class.java, "library_database")
                    .fallbackToDestructiveMigration()
                    .build()
            }

            return database!!
        }
    }
}