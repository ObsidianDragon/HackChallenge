package com.example.hackchallenge

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Credentials
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AskFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AskFragment : Fragment() {
    private val client = OkHttpClient()

    interface Callback {
        fun onClick()
    }

    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
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
            //TODO Networking
            runBlocking {
                withContext(Dispatchers.IO) {
//                    postQuestion(editText.text.toString())
                }
            }

            //TODO go to question history
            callback.onClick()
        }
        // Inflate the layout for this fragment
        return rootView
    }

    private fun postQuestion(question: String) {

        val postBody = "{\"body\": $question}"

        val request = Request.Builder()
            .url(BASE_URL + "question/")
//            .post(RequestBody.create(MediaType.parse("text/x-markdown", postBody))
            .post(postBody.toRequestBody(("text/x-markdown").toMediaType()))
            .build()

        client.newCall(request).execute().use { response ->
            Log.d("response", response.body!!.string())
            if (!response.isSuccessful) {
                Log.d("ERROR", "Unexpected code $response")
                //TODO handle failure?
            }
            //TODO what happens after asking?

        }
    }
}