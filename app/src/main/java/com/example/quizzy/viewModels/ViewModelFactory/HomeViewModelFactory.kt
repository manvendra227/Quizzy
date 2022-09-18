package com.example.quizzy.viewModels.ViewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quizzy.viewModels.HomeViewModel
import com.example.quizzy.viewModels.QuizDetailViewModel

class HomeViewModelFactory(private val emailId: String) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(emailId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}