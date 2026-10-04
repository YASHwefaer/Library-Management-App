package com.example.library_menagement_app.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.library_menagement_app.Databse.AppDatabase
import com.example.library_menagement_app.Model.User
import com.example.library_menagement_app.databinding.ActivityRegisterBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Register : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {

            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val Confirmpassword = binding.etConfirmPassword.text.toString()

            if (name.isEmpty()) {
                binding.etName.error = "Enter name"
            }

            else if (email.isEmpty()) {
                binding.etEmail.error = "Enter Email"
            }

            else if (password.isEmpty()) {
                binding.etPassword.error = "Pls fill the Password!"
            }

            else if (Confirmpassword.isEmpty()) {
                binding.etConfirmPassword.error = "Pls fill the confirmPassword!"
            }

            else if (password != Confirmpassword) {
                binding.etConfirmPassword.error = "Password does not match"
            }

            else {

                val user = User(
                    name = name,
                    email = email,
                    password = password
                )

                val databasse = AppDatabase.getDatabase(this)

                val userDao = databasse.userDao()

                lifecycleScope.launch(Dispatchers.IO) {

                    userDao.insertUser(user)

                    runOnUiThread {

                        Toast.makeText(this@Register, "Register successfull!", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@Register, LoginPage::class.java)

                        startActivity(intent)
                        finish()
                    }
                }
            }
        }

        binding.tvLogin.setOnClickListener {

            val intent = Intent(this@Register, LoginPage::class.java)

            startActivity(intent)
            finish()
        }
    }
}