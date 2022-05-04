package com.example.hackchallenge

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

const val BASE_URL = "https://10.48.67.226/api"

class LoginActivity : AppCompatActivity() {


    private val client = OkHttpClient()

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
            //Networking calls
            runBlocking {
                withContext(Dispatchers.IO) {
                    if (signup) {
                        postUserAuth(username.text.toString(), password.text.toString())
                    } else {
                        getUserAuth(username.text.toString(), password.text.toString())
                    }
                }
            }
//            val intent = Intent(this, MainActivity::class.java)
//            intent.putExtra("username", username.text)
//            startActivity(intent)
        }


    }

//    private fun getUser(username: String, password: String) {
//        Log.d("In getUser", username+password)
//        val requestGet = Request.Builder()
//            .url(BASE_URL+"user/")
//            .header("id", username)
//            .header("password", password)
//            .build()
//        client.newCall(requestGet).enqueue(object: Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                e.printStackTrace()
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                response.use{
//                    if(!it.isSuccessful){
//                        Log.d("Get user failed", response.toString())
//                        //TODO handle failure?
//                    } else {
//                        nextPage(username)
//                    }
//                }
//            }
//
//        })
//    }
//
//    private fun postUser(username: String, password: String) {
//
//        val postBody = "{\"username\": \""+username+"\"}"
//
//        val request = Request.Builder()
//            .url(BASE_URL + "user/").header("password", password)
//            .post(postBody.toRequestBody(("application/json; charset=utf-8").toMediaType()))
//            .build()
//
//        client.newCall(request).execute().use { response ->
//            if (!response.isSuccessful) Log.d("ERROR", "Unexpected code $response")
//            Log.d("response", response.body!!.string())
//        }
//    }

    private fun postUserAuth(username: String, password: String) {

        val postBody = ""

        val request = Request.Builder()
            .url(BASE_URL + "users/")
            .addHeader("Authorization", Credentials.basic(username, password))
//            .post(RequestBody.create(MediaType.parse("text/x-markdown", postBody))
            .post(postBody.toRequestBody(("text/x-markdown").toMediaType()))
            .build()

        client.newCall(request).execute().use { response ->
            Log.d("response", response.body!!.string())
            if (!response.isSuccessful) {
                Log.d("ERROR", "Unexpected code $response")
                //TODO handle failure?
            } else {
                nextPage(username)
            }

        }
    }

    private fun getUserAuth(username: String, password: String) {
        Log.d("In getUserAuth", username+password)
        val requestGet = Request.Builder()
            .url(BASE_URL+"user/"+username.hashCode())
            .addHeader("Authorization", Credentials.basic(username, password))
            .build()
        client.newCall(requestGet).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                Log.d("failure", e.toString())
                nextPage(username)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use{
                    if(!it.isSuccessful){
                        Log.d("Get user failed", response.toString())
                        //TODO handle failure?
                    } else {
                        nextPage(username)
                    }
                }
            }

        })
    }

    private fun nextPage(username: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
    }
}