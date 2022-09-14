package com.example.quizzy.DataModel.Model

import com.example.quizzy.DataModel.Enums.Gender
import com.example.quizzy.DataModel.Enums.Status
import java.util.*

data class UserModel(
    var name: String,
    var emailId: String,
    var dob: Date,
    var password: String,
    var gender: Gender,
    var status: Status
)