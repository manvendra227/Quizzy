package com.example.quizzy.dataModel.model

import java.util.*

data class AttemptModelUser(
    val quizID: String,
    val title: String,
    val score: Double,
    val passingScore: Double,
    val time: Long,
    val startTime: Date
)