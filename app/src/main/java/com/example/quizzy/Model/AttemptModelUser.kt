package com.example.quizzy.Model

data class AttemptModelUser (
    private val quizID: String,
    private val title: String,
    private val score: Double,
    private val time: Long,
    private val feedback: Double
)