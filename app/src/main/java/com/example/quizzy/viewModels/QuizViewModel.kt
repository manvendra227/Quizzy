package com.example.quizzy.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizzy.dataModel.model.ProgressModel
import com.example.quizzy.dataModel.model.QuizShortModel

class QuizViewModel:ViewModel() {

    private var progressList: MutableLiveData<List<ProgressModel>?> = MutableLiveData()

    fun getProgressList() :MutableLiveData<List<ProgressModel>?>{
        return progressList
    }

}