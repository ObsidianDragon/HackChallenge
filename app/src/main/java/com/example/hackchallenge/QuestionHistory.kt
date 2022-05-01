package com.example.hackchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import java.io.IOException

const val BASE_URL = "http://"

class QuestionHistory : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: QuestionAdapter

    private val client = OkHttpClient()
    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val questionJsonAdapter = moshi.adapter(Question::class.java)
    private val questionListType = Types.newParameterizedType(List::class.java, Question::class.java)
    private val questionListJsonAdapter : JsonAdapter<List<Question>> = moshi.adapter(questionListType)
    private val notes : MutableList<Question> = mutableListOf<Question>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_history)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        populateQuestionsList()
    }

    private fun populateQuestionsList() {
        //TODO networking calls
        val requestGet = Request.Builder().url(BASE_URL+"posts/").build()
        client.newCall(requestGet).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use{
                    if(!it.isSuccessful){
                        Log.d("Populate unsuccessful", response.toString())
                    }
                    val noteList = questionListJsonAdapter.fromJson(response.body!!.string())!!
                    for(note in noteList) {
                        notes.add(note)
                    }
                    adapter = QuestionAdapter(notes)
                    runOnUiThread{
                        recyclerView.adapter = adapter
                    }
                }
            }

        })
    }
}