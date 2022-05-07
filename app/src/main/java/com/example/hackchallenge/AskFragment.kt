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
import android.widget.EditText
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Credentials
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody


class AskFragment : Fragment() {
    private val client = OkHttpClient()

    interface Callback {
        fun onClick()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val callback = activity as Callback
        val rootView = inflater.inflate(R.layout.fragment_ask, container, false)
        val button: Button = rootView.findViewById(R.id.button)
        val editText: EditText = rootView.findViewById(R.id.question)

        button.setOnClickListener{
            if (!editText.text.isBlank()) {
                runBlocking {
                    withContext(Dispatchers.IO) {
                        postQuestion(editText.text.toString())
                    }
                }

                callback.onClick()
            }
        }
        // Inflate the layout for this fragment
        return rootView
    }

    private fun postQuestion(question: String) {

        val postBody = "{\"description\": \"$question\", \"response\": \"0\"}"

        val request = Request.Builder()
            .url(BASE_URL + "api/questions/")
            .addHeader("Authorization", this.requireActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE).getString("token", "")!!)
            .post(postBody.toRequestBody(("text/x-markdown").toMediaType()))
            .build()

        client.newCall(request).execute().use { response ->
            Log.d("response", response.body!!.string())
            if (!response.isSuccessful) {
                Log.d("ERROR", "Unexpected code $response")
//                val text = "Could not ask your question. Please try again later."
//                val duration = Toast.LENGTH_SHORT
//                val toast = Toast.makeText(this.requireContext(), text, duration)
//                toast.show()
            }
        }
    }
}