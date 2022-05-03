package com.example.hackchallenge

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val login: Button = findViewById(R.id.login)
        val signup: Button = findViewById(R.id.signup)

        login.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("signup", false)
            startActivity(intent)
        }

        signup.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("signup", true)
            startActivity(intent)
        }
    }
}