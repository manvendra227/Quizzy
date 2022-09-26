package com.example.quizzy.dataModel.model

import java.util.*

class AttemptModelQuiz(
    val userId: String,
    val username: String,
    val score: Double,
    val time: Long,
    val feedback: Double,
    val startTime:Date
)