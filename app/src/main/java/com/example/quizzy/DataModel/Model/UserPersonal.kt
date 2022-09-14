package com.example.quizzy.DataModel.Model

data class UserPersonal(
    var noOfQuestions:Int,
    var correctQuestion:Int,
    var wishlist:List<String>?,
    var search:List<String>?
)
