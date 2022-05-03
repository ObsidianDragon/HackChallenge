package com.example.hackchallenge

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

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

    interface Callback {
        fun onClick(src: Int)
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

        button.setOnClickListener{
            //TODO Networking
            //TODO go to question history
        }
        // Inflate the layout for this fragment
        return rootView
    }
}