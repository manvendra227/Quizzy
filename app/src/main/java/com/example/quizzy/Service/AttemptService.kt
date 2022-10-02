package com.example.quizzy.Service

import com.example.quizzy.dataModel.entity.Attempt
import com.example.quizzy.dataModel.model.AttemptModelQuiz
import com.example.quizzy.dataModel.model.AttemptModelQuizUser
import com.example.quizzy.dataModel.model.AttemptModelUser
import com.example.quizzy.dataModel.model.AttemptSaveModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AttemptService {

    @POST("/attempt")
    fun saveAttempt(@Body attempt: AttemptSaveModel): Call<Unit>

    @GET("/attempt/quizAttempts")
    suspend fun fetchQuizAttempt(@Query("quizId")quizId:String):Response<List<AttemptModelQuiz>>

    @GET("/attempt/userAttempts")
    suspend fun fetchUserAttempt(@Query("userId")userId:String):Response<List<AttemptModelUser>>

    @GET("attempt/userAttemptsOnQuiz")
    suspend fun fetchUserAttemptOnQuiz(@Query("quizId")quizId:String,@Query("userId")userId:String):Response<List<AttemptModelQuizUser>>


}