package com.example.quizzy.dataModel.model

import com.example.quizzy.dataModel.enums.Difficulty
import java.util.*

data class QuizShortModel(
    val quizId: String,
    val title: String,
    val description: String,
    val difficulty: Difficulty,
    val authorID: String,
    val authorName: String,
    val timesPlayed: Int,
    val avgRating: Double,
    val timestamp: Date? = null
)