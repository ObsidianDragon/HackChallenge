package com.example.hackchallenge

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Question(
    val body: String,
    val answer: String
)