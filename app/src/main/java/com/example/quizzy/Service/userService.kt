package com.example.quizzy.Service

import com.example.quizzy.dataModel.entity.User
import com.example.quizzy.dataModel.model.LoginModel
import retrofit2.Call
import retrofit2.http.*

interface userService {

    @POST("user")
    fun saveUser(@Body user: User): Call<String>

    @GET("user/login")
    fun loginUser(@Query("emailId")emailId:String,@Query("password") password:String):Call<String>
}