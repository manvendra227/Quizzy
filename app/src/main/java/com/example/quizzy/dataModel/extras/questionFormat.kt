package com.example.quizzy.dataModel.extras

data class questionFormat(
    val question: String,
    val options: List<String>? = null,
    val answer: Int,
    val explanation: String
)