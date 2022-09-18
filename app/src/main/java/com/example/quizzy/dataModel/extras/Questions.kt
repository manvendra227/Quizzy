package com.example.quizzy.dataModel.extras


data class Questions (
    private val noOfQuestions:Int,
    private val question: List<questionFormat>? = null,
    private val score: Score? = null
)