package com.example.quizzy.dataModel.extras

data class Score (
    private val maxScore: Double,
    private val passingScore: Double,
    private val onCorrect: Double,
    private val onWrong: Double
)