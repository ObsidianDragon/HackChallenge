package com.example.hackchallenge

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

const val BASE_URL = "http://34.130.39.222/"

class LoginActivity : AppCompatActivity() {


    private val client = OkHttpClient()
    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //access token from shared preferences
        sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        setContentView(R.layout.activity_login)

        val username: EditText = findViewById(R.id.username)
        val password: EditText = findViewById(R.id.password)
        val button: Button = findViewById(R.id.button)
        val message: TextView = findViewById(R.id.accountMessage)
        val link: TextView = findViewById(R.id.signUpText)



        //change login to setup
        var signup = intent.getBooleanExtra("signup", false)
        if (signup) {
            button.text = getString(R.string.create)
            message.text = getString(R.string.already_have_an_account)
            link.text = getString(R.string.log_in)
        }

        button.setOnClickListener {
            //Networking calls
            runBlocking {
                withContext(Dispatchers.IO) {
                    if (signup) {
                        postUserAuth(username.text.toString(), password.text.toString())
                    } else {
                        loginAuth(username.text.toString(), password.text.toString())
                    }
                }
            }
        }

        //change login to signup and vice versa
        link.setOnClickListener {
            signup = !signup
            if (signup) {
                button.text = getString(R.string.create)
                message.text = getString(R.string.already_have_an_account)
                link.text = getString(R.string.log_in)
            } else {
                button.text = getString(R.string.log_in)
                message.text = getString(R.string.don_t_have_an_account)
                link.text = getString(R.string.sign_up)
            }
        }


    }

    //method for signing up a new account
    private fun postUserAuth(username: String, password: String) {

        val postBody = "{\"username\": \"$username\", \"password\": \"$password\"}"

        val request = Request.Builder()
            .url(BASE_URL + "register/")
            .addHeader("Authorization", Credentials.basic(username, password))
            .post(postBody.toRequestBody(("text/x-markdown").toMediaType()))
            .build()

        client.newCall(request).execute().use { response ->
            val jsonData = response.body!!.string()
            Log.d("response", jsonData)
            if (!response.isSuccessful) {
                Log.d("ERROR", "Unexpected code $response")

                val text = "This username already exists. Please pick another"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(applicationContext, text, duration)
                toast.show()
            } else {
                val json = JSONObject(jsonData)
                sharedPref.edit().putString("token", json.getString("session token")).commit()
                Log.d("token response", json.getString("session token"))
                sharedPref.edit().putString("update_token", json.getString("update_token")).commit()
                nextPage()
            }
        }
    }

    private fun loginAuth(username: String, password: String) {

        val postBody = "{\"username\": \"$username\", \"password\": \"$password\"}"

        val request = Request.Builder()
            .url(BASE_URL + "login/")
            .addHeader("Authorization", Credentials.basic(username, password))
            .post(postBody.toRequestBody(("text/x-markdown").toMediaType()))
            .build()

        client.newCall(request).execute().use { response ->
            val jsonData = response.body!!.string()
            Log.d("response", jsonData)
            if (!response.isSuccessful) {
                Log.d("ERROR", "Unexpected code $response")

                val text = "Invalid username or password"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(applicationContext, text, duration)
                toast.show()
            } else {
                val json = JSONObject(jsonData)
                sharedPref.edit().putString("token", json.getString("session token")).commit()
                sharedPref.edit().putString("update_token", json.getString("update_token")).commit()
                nextPage()
            }

        }
    }

    private fun nextPage() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}