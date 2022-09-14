package com.example.quizzy.Model

import com.example.quizzy.Model.Enums.Gender
import com.example.quizzy.Model.Enums.Status
import java.util.*

data class UserModel(
    var name: String,
    var emailId: String,
    var dob: Date,
    var password: String,
    var gender: Gender,
    var status: Status
)