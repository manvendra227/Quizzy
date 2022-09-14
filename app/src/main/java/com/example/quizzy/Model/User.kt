package com.example.quizzy.Model

import java.sql.Timestamp
import java.util.*

data class User(
    var userId:String?="",
    var name:String?="",
    var emailID:String?="",
    var password:String?="",
    var gender: Gender=Gender.MALE,
//    var date_of_birth:Date,
//    var isVerified:Boolean=false,
//    var timestamp: Timestamp,
    var userPersonal: UserPersonal
)
