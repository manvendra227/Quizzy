package com.example.quizzy.dataModel.model

import com.example.quizzy.dataModel.enums.Gender
import com.example.quizzy.dataModel.enums.Status
import java.util.*

data class UserModel(
    var name: String,
    var emailId: String,
    var dob: String,
    var password: String,
    var matchingPassword:String,
    var gender: Gender,
    var status: Status
)