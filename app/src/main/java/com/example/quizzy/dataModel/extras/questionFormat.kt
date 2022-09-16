package com.example.quizzy.dataModel.extras

data class questionFormat (
    private val question: String,
    private val options: List<String>? = null,
    private val answer: String,
    private val explanation: String
)