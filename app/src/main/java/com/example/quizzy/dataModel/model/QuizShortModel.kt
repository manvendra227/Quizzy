package com.example.quizzy.dataModel.model

import com.example.quizzy.dataModel.enums.Difficulty
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