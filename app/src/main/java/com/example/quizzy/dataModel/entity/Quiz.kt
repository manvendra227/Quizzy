package com.example.quizzy.dataModel.entity

import com.example.quizzy.dataModel.enums.Difficulty
import com.example.quizzy.dataModel.enums.QuizType
import com.example.quizzy.dataModel.extras.Questions
import java.util.*


data class Quiz(
    val quizID: String,
    val title: String,
    val description: String="",
    val difficulty: Difficulty? = null,
    val quizType: QuizType? = null,
    val questions: Questions? = null,
    val authorID: String,
    val authorName: String,
    val tags: List<String>? = null,
    val time: Int,
    val timestamp: Date? = null,
    val isImported: Boolean = false,
    val timesPlayed: Int,
    val avgRating: Double
)