package com.example.hackchallenge

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.*
import java.io.IOException


class HistoryFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: QuestionAdapter

    private val client = OkHttpClient()
    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val questionListJsonAdapter = moshi.adapter(QuestionList::class.java)
    private val questions : MutableList<Question> = mutableListOf<Question>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        runBlocking {
            withContext(Dispatchers.IO) {
                populateQuestionList()
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_history, container, false)
        recyclerView = rootView.findViewById(R.id.recyclerView)
        adapter = QuestionAdapter(questions)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = adapter


        // Inflate the layout for this fragment
        return rootView
    }

    private fun populateQuestionList() {
        val requestGet = Request.Builder()
            .url(BASE_URL+"api/questions/asked/")
            .addHeader("Authorization", "Bearer " + this.requireActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE).getString("token", ""))
            .build()
        Log.d("sending token: ", this.requireActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE).getString("token", "")!!)
        client.newCall(requestGet).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use{
                    if(!it.isSuccessful){
                        Log.d("Populate unsuccessful", response.toString())
                    }
                    val questionList = questionListJsonAdapter.fromJson(response.body!!.string())!!
                    adapter = QuestionAdapter(questionList.asked)
                    runBlocking {
                        withContext(Dispatchers.Main) {
                            recyclerView.adapter = adapter
                        }
                    }
                }
            }

        })
    }
}