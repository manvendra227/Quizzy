package com.example.quizzy.Service

import com.example.quizzy.dataModel.entity.Attempt
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AttemptService {

    @POST("/attempt")
    fun saveAttempt(@Body attempt: Attempt): Call<Unit>
}