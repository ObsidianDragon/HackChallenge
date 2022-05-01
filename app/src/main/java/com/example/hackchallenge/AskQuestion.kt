package com.example.hackchallenge

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AskQuestion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ask_question)

        val button: Button = findViewById(R.id.button)

        button.setOnClickListener{
            //TODO Networking
            //TODO replace this intent
            val intent = Intent(this, QuestionHistory::class.java)
            startActivity(intent)
        }
    }
}