package com.example.hackchallenge

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: QuestionAdapter

    private val client = OkHttpClient()
    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val questionJsonAdapter = moshi.adapter(Question::class.java)
    private val questionListType = Types.newParameterizedType(List::class.java, Question::class.java)
    private val questionListJsonAdapter : JsonAdapter<List<Question>> = moshi.adapter(questionListType)
    private val questions : MutableList<Question> = mutableListOf<Question>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
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

        // Inflate the layout for this fragment
        return rootView
    }

    private fun populateQuestionList() {
        val requestGet = Request.Builder()
            .url(BASE_URL+"question/")
            .build()
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
                    for(note in questionList) {
                        questions.add(note)
                    }
                    adapter = QuestionAdapter(questions)
                    recyclerView.adapter = adapter
                }
            }

        })
    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment HistoryFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            HistoryFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}