package com.example.quizzy.dataModel.entity

import java.time.LocalDateTime
import java.util.*

data class Attempt(
    private val userId: String,
    private val quizId: String,
    private val score: Double,
    private val startTime: String?,
    private val endTime: String?,
    private val feedback: Double
)