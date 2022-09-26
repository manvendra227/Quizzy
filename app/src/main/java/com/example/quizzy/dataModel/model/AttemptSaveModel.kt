package com.example.quizzy.dataModel.model

data class AttemptSaveModel(
    private val userId: String,
    private val quizId: String,
    private val score: Double,
    private val startTime: String?,
    private val endTime: String?,
    private val feedback: Double,
    private val newQuestions:Int?,
    private val newCorrect: Int?
)