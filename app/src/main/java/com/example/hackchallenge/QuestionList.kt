package com.example.hackchallenge

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuestionList(
    val asked: List<Question>
)