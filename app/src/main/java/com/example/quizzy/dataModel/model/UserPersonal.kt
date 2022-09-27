package com.example.quizzy.dataModel.model

data class UserPersonal(
    var questionsSolved:Int,
    var questionsCorrect:Int,
    var wishlist:List<String>?,
    var search:List<String>?
)
