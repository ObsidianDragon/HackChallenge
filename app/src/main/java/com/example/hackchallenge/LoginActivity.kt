package com.example.hackchallenge

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val username: EditText = findViewById(R.id.username)
        val password: EditText = findViewById(R.id.password)
        val button: Button = findViewById(R.id.button)
        val message: TextView = findViewById(R.id.message)

        val signup = intent.getBooleanExtra("signup", false)
        if (signup) {
            button.text = getString(R.string.sign_up)
            message.text = getString(R.string.sign_up)
        }

        button.setOnClickListener {
            //TODO Networking calls
            val intent = Intent(this, AskQuestion::class.java)
            intent.putExtra("username", username.text)
            startActivity(intent)
        }


    }
}