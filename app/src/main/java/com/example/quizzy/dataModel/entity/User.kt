package com.example.quizzy.dataModel.entity

import com.example.quizzy.dataModel.enums.Gender
import com.example.quizzy.dataModel.enums.Status
import com.example.quizzy.dataModel.model.UserPersonal
import java.sql.Date
import java.sql.Timestamp

data class User(
    var userId:String,
    var name:String,
    var emailID:String,
    var password:String,
    var enabled:Boolean,
    var gender: Gender,
    var date_of_birth:Gender,
    var staus:Status,
    var isVerfied:Boolean,
    var timestamp: java.util.Date,
    var userPersonal: UserPersonal)
