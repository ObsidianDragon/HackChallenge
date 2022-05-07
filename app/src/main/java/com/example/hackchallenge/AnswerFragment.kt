package com.example.hackchallenge

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException


class AnswerFragment : Fragment() {

    lateinit var question : TextView
    private val client = OkHttpClient()
    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val questionJsonAdapter = moshi.adapter(Question::class.java)
    lateinit var questionId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //request a first question to display
        getQuestion()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_answer, container, false)
        question = rootView.findViewById(R.id.question)
        question.text = getString(R.string.loading)
        val yesButton = rootView.findViewById<Button>(R.id.yesButton)
        val noButton = rootView.findViewById<Button>(R.id.noButton)
        val skipButton = rootView.findViewById<Button>(R.id.skipButton)

        //set listeners to post the respective response value
        yesButton.setOnClickListener{
            runBlocking {
                withContext(Dispatchers.IO) {
                    postResponse(0)
                }
            }
        }
        noButton.setOnClickListener{
            runBlocking {
                withContext(Dispatchers.IO) {
                    postResponse(1)
                }
            }
        }
        skipButton.setOnClickListener{
            runBlocking {
                withContext(Dispatchers.IO) {
                    postResponse(2)
                }
            }
        }


        // Inflate the layout for this fragment
        return rootView
    }

    //Method to answer a question using authentication
    private fun postResponse(answer: Int) {

        if (questionId.equals("-1")) {
            return
        }
        val postBody = "{\"response\": \""+answer.toString()+"\"}"
        Log.d("postBody", postBody)

        //adding token in the header to authenticate transaction
        val request = Request.Builder()
            .url(BASE_URL + "api/questions/"+questionId+"/")
            .addHeader("Authorization", "Bearer " + this.requireActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE).getString("token", ""))
            .post(postBody.toRequestBody(("text/x-markdown").toMediaType()))
            .build()
        Log.d("token", "Bearer " + this.requireActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE).getString("Token", ""))

        client.newCall(request).execute().use { response ->
            val jsonData = response.body!!.string()
            val json = JSONObject(jsonData)
            Log.d("response", jsonData)
            if (!response.isSuccessful) {
                Log.d("ERROR", "Unexpected code $response")
                if (json.getString("error").equals("Invalid session token")) {
                    //update token
                    postUpdate(answer)
                } else if (json.getString("error").equals("Question has already been responded to!")) {
                    getQuestion()
                }
            } else {
                //if successful, get a new question to answer
                getQuestion()
            }

        }
    }

    //Method to use the update token
    private fun postUpdate(answer: Int) {

        val postBody = ""
        val sharedPref = this.requireActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        val request = Request.Builder()
            .url(BASE_URL + "session/")
            .addHeader("Authorization", "Bearer " + sharedPref.getString("update_token", ""))
            .post(postBody.toRequestBody(("text/x-markdown").toMediaType()))
            .build()
        Log.d("update_token", "Bearer " + this.requireActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE).getString("update_token", ""))

        client.newCall(request).execute().use { response ->
            val jsonData = response.body!!.string()
            Log.d("response", jsonData)
            if (!response.isSuccessful) {
                Log.d("ERROR", "Unexpected code $response")
                goToLogin()
            } else {
                //update tokens if successful
                val json = JSONObject(jsonData)

                sharedPref.edit().putString("token", json.getString("session token")).commit()
                sharedPref.edit().putString("update_token", json.getString("update_token")).commit()
                postResponse(answer)
            }

        }
    }

    //get and display a new question. Also saves question id for responses
    private fun getQuestion() {
        val requestGet = Request.Builder()
            .url(BASE_URL+"api/questions/next/")
            .addHeader("Authorization", this.requireActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE).getString("token", "").toString())
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
                        Log.d("Get next question failed", response.toString())
                        question.text = getString(R.string.congrats)
                        questionId = "-1"
                    } else {

                        val jsonData = response.body!!.string()
                        val json = JSONObject(jsonData)
                        val newQuestion = questionJsonAdapter.fromJson(json.getJSONObject("question").toString())!!
                        Log.d("success", newQuestion.description)
                        questionId = newQuestion.id
                        question.text = newQuestion.description
                    }
                }
            }

        })
    }

    private fun goToLogin() {
        val intent = Intent(this.requireContext(), LoginActivity::class.java)
        startActivity(intent)
    }
}