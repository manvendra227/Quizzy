package com.example.quizzy.Model

import java.util.*

data class AttemptModelQuizUser (
    private val timestamp: Date? = null,
    private val score: Double,
    private val time: Long
)