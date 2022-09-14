package com.example.quizzy.DataModel.Model

import com.example.quizzy.DataModel.Enums.Difficulty
import java.util.*

data class QuizShortModel (
        private val quizId: String,
        private val title: String,
        private val description: String,
        private val difficulty: Difficulty,
        private val authorID: String,
        private val authorName: String,
        private val timesPlayed:Int,
        private val avgRating:Double,
        private val timestamp: Date? = null
)