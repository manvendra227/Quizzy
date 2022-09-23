package com.example.quizzy.viewModels.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quizzy.viewModels.QuizViewModel

class QuizViewModelFactory( val quizId: String,val time:String) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            return QuizViewModel(quizId,time) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}