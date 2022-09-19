package com.example.quizzy.Service

import com.example.quizzy.dataModel.entity.Quiz
import com.example.quizzy.dataModel.entity.User
import com.example.quizzy.dataModel.model.QuizShortModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface QuizService {

    @GET("quiz/load")
    fun loadQuiz(@Query("page") page:Int): Call<List<QuizShortModel>>

    @GET("quiz/search")
    fun loadSearch(@Query("searchKey") searchKey:String):Call<List<QuizShortModel>>

    @GET("quiz")
    fun fetchQuizById(@Query("id") quizId:String):Call<Quiz>
}