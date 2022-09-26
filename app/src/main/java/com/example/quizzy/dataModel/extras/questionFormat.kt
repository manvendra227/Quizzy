package com.example.quizzy.dataModel.extras

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

 data class questionFormat(
    val question: String,
    val options: List<String>? = null,
    val answer: String,
    val explanation: String
)