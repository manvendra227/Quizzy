package com.example.quizzy.dataModel.model

import com.example.quizzy.dataModel.enums.Progress

data class ProgressModel(
    val progress:Progress=Progress.UNMARKED
)