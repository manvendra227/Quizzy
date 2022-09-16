package com.example.quizzy.Service

import com.example.quizzy.dataModel.model.QuizShortModel
import retrofit2.Call
import retrofit2.http.GET

interface QuizService {

    @GET("quiz/load")
    fun loadQuiz(): Call<List<QuizShortModel>>
}