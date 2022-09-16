package com.example.quizzy.dataModel.entity

import com.example.quizzy.dataModel.enums.Gender
import com.example.quizzy.dataModel.model.UserPersonal

data class User(
    var userId:String?="",
    var name:String?="",
    var emailID:String?="",
    var password:String?="",
    var gender: Gender = Gender.MALE,
//    var date_of_birth:Date,
//    var isVerified:Boolean=false,
//    var timestamp: Timestamp,
    var userPersonal: UserPersonal
)
