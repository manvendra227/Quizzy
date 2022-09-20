package com.example.quizzy.dataModel.extras

data class Score(
    val maxScore: Int,
    val passingScore: Double,
    val onCorrect: Int,
    val onWrong: Int
)