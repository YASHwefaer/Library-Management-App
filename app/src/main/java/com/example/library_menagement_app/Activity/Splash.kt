package com.example.library_menagement_app.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.library_menagement_app.databinding.SplashBinding

class Splash : AppCompatActivity() {

    private lateinit var binding: SplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("login", MODE_PRIVATE)

        val isLogin = sharedPreferences.getBoolean("islogin", false)

        Handler(Looper.getMainLooper()).postDelayed({

            if (isLogin) {

                val intent = Intent(this, HomeActivity::class.java)

                startActivity(intent)

            } else {

                val intent = Intent(this, LoginPage::class.java)

                startActivity(intent)
            }

            finish()

        }, 2000)
    }
}