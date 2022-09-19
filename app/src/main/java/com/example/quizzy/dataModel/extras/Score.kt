package com.example.quizzy.dataModel.extras

data class Score(
    val maxScore: Double,
    val passingScore: Double,
    val onCorrect: Double,
    val onWrong: Double
)