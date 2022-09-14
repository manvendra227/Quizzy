package com.example.quizzy.Service

import com.example.quizzy.DataModel.Entity.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

    @POST("user")
    fun saveUser(@Body user: User): Call<String>
}