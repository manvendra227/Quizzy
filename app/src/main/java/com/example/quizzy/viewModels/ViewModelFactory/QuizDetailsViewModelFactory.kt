package com.example.quizzy.viewModels.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quizzy.viewModels.QuizDetailViewModel

class QuizDetailsViewModelFactory(val quizId: String,val userId: String) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizDetailViewModel::class.java)) {
            return QuizDetailViewModel(quizId, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}