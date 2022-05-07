package com.example.hackchallenge

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Question(
    val description: String,
    val response: Int,
    val id: String,
    val answered: String?
)