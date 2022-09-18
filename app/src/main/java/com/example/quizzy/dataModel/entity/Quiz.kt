package com.example.quizzy.dataModel.entity

import com.example.quizzy.dataModel.enums.Difficulty
import com.example.quizzy.dataModel.enums.QuizType
import com.example.quizzy.dataModel.extras.Questions
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