package com.example.quizzy.dataModel.model

data class UserPersonal(
    var noOfQuestions:Int,
    var correctQuestion:Int,
    var wishlist:List<String>?,
    var search:List<String>?
)
