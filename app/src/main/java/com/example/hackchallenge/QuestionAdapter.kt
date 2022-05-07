package com.example.hackchallenge

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class QuestionAdapter(private val questions: List<Question>) : RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {
    class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val body: TextView = itemView.findViewById(R.id.body)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.question_cell, parent, false) as View
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        Log.d("In bindviewholder", position.toString())

        val question = questions[position]

//        Log.d("In bindviewholder", question.response.toString())
        holder.body.text = question.description
        if (question.answered != null && question.response == 0) {
            holder.body.setBackgroundColor(ContextCompat.getColor(holder.body.context, R.color.green))
//            Log.d("In bindviewholder", question.description+" is green")
        } else if (question.answered != null && question.response == 1) {
            holder.body.setBackgroundColor(ContextCompat.getColor(holder.body.context, R.color.red))
//            Log.d("In bindviewholder", question.description+" is red")
        } else if (question.answered != null && question.response == 2) {
            holder.body.setBackgroundColor(ContextCompat.getColor(holder.body.context, R.color.medium_blue))
//            Log.d("In bindviewholder", question.description+" is blue")
        }
        if (question.answered == null) {
            holder.body.setBackgroundColor(ContextCompat.getColor(holder.body.context, R.color.trans))
        }
    }

    override fun getItemCount(): Int {
        return questions.size
    }
}