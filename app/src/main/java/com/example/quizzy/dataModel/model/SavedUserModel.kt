package com.example.quizzy.dataModel.model

import com.example.quizzy.dataModel.enums.Gender
import com.example.quizzy.dataModel.enums.Status

data class SavedUserModel(
    var userId:String="",
    var name: String="",
    var emailId: String=""
)