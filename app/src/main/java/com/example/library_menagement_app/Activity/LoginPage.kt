package com.example.library_menagement_app.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.library_menagement_app.Databse.AppDatabase
import com.example.library_menagement_app.databinding.ActivityLoginPageBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginPage : AppCompatActivity() {

    private lateinit var binding: ActivityLoginPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignIn.setOnClickListener {

            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty()) {

                binding.etEmail.error = "Enter Email"

            } else if (password.isEmpty()) {

                binding.etPassword.error = "Enter Password!"

            } else {

                val database = AppDatabase.getDatabase(this)
                val userDao = database.userDao()

                lifecycleScope.launch(Dispatchers.IO) {

                    val user = userDao.loginUser(email, password)

                    runOnUiThread {

                        if (user != null) {

                            val sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)

                            sharedPreferences.edit()
                                .putBoolean("islogin", true)
                                .putString("name", user.name)
                                .putString("email", user.email)
                                .putInt("userId", user.id)
                                .apply()

                            Toast.makeText(this@LoginPage, "Login successfully", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this@LoginPage, HomeActivity::class.java)

                            startActivity(intent)
                            finish()

                        } else {

                            Toast.makeText(this@LoginPage, "Invalid Email or Password", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        binding.BtnSignup.setOnClickListener {

            val intent = Intent(this@LoginPage, Register::class.java)

            startActivity(intent)
        }
    }
}