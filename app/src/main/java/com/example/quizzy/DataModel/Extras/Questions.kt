package com.example.quizzy.DataModel.Extras


data class Questions (
    private val noOfQuestions:Int,
    private val question: List<questionFormat>? = null,
    private val score: Score? = null
)