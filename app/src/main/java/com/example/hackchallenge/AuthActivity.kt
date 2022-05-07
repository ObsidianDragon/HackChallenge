package com.example.hackchallenge

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class AuthActivity : AppCompatActivity() {

    private val client = OkHttpClient()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //check for token and skip login page if it is valid
        checkToken()

        setContentView(R.layout.activity_auth)

        val login: Button = findViewById(R.id.login)
        val signup: Button = findViewById(R.id.signup)

        //listeners to go to login or signup page
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

    private fun checkToken() {
        val requestGet = Request.Builder()
            .url(BASE_URL+"api/users/id/")
            .addHeader("Authorization", this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE).getString("token", "").toString())
            .build()
        client.newCall(requestGet).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                Log.d("failure", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                response.use{
                    if(!it.isSuccessful){
                        //display no questions left message
                        Log.d("Invalid Token?", response.toString())
                    } else {
                        //if token is valid, skip login
                        skipLogin()
                    }
                }
            }
        })
    }

    private fun skipLogin() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}