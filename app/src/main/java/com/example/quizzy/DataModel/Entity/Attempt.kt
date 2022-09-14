package com.example.quizzy.DataModel.Entity

import java.util.*

data class Attempt (
    private val userId: String,
    private val quizId: String,
    private val score: Double,
    private val startTime: Date? = null,
    private val endTime: Date? = null,
    private val feedback: Double
)