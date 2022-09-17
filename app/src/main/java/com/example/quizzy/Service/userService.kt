package com.example.quizzy.Service

import com.example.quizzy.dataModel.entity.User
import com.example.quizzy.dataModel.model.LoginModel
import com.example.quizzy.dataModel.model.QuizShortModel
import com.example.quizzy.dataModel.model.UserModel
import retrofit2.Call
import retrofit2.http.*

interface userService {

    @POST("user")
    fun saveUser(@Body userModel: UserModel): Call<String>

    @GET("user/login")
    fun loginUser(@Query("emailId")emailId:String,@Query("password") password:String):Call<String>

    @GET("user/userpersonal/searchlist")
    fun fetchSearchTags(@Query("emailId")emailId:String):Call<List<String>>

}