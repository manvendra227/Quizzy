package com.example.quizzy.dataModel.model

import java.util.*

data class AttemptModelQuizUser (
    private val timestamp: Date? = null,
    private val score: Double,
    private val time: Long
)