package com.example.quizzy.DataModel.Entity

import com.example.quizzy.DataModel.Enums.Difficulty
import com.example.quizzy.DataModel.Enums.QuizType
import com.example.quizzy.DataModel.Extras.Questions
import java.util.*


data class Quiz (

    private val quizID: String,
    private val title: String,
    private val description: String,
    private val difficulty: Difficulty? = null,
    private val quizType: QuizType? = null,
    private val questions: Questions? = null,
    private val authorID: String,
    private val authorName: String,
    private val tags: List<String>? = null,
    private val time:Int,
    private val timestamp: Date? = null,
    private val isImported: Boolean = false,
    private val timesPlayed:Int,
    private val avgRating:Double
)