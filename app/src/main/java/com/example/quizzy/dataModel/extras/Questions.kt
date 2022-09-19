package com.example.quizzy.dataModel.extras


data class Questions(
    val noOfQuestions: Int,
    val question: List<questionFormat>? = null,
    val score: Score? = null
)