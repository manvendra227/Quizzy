package com.example.quizzy.viewModels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizzy.Service.AttemptService
import com.example.quizzy.Service.RetrofitBuilder
import com.example.quizzy.Service.userService
import com.example.quizzy.dataModel.entity.User
import com.example.quizzy.dataModel.extras.questionFormat
import com.example.quizzy.dataModel.model.AttemptModelUser
import com.example.quizzy.dataModel.model.ProgressModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(val userId: String) :ViewModel() {


    private val attemptService = RetrofitBuilder.buildService(AttemptService::class.java)
    private val userService = RetrofitBuilder.buildService(userService::class.java)
    private var list: MutableLiveData<List<AttemptModelUser>?> = MutableLiveData()
    private lateinit var user: User



    init {
        fetchUser()
        fetchUserAttempts()
    }


    fun getUserAttemptList(): MutableLiveData<List<AttemptModelUser>?> {
        return list
    }


    private fun fetchUser(){
        val request=userService.getUserById(userId)
        request.enqueue(object: Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {

                if (response.isSuccessful){
                    user=response.body()!!
                    Log.i("message",user.toString())
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.i("error","error user by ID")
            }
        } )
    }

    private fun fetchUserAttempts(){
        val request=attemptService.fetchUserAttempt(userId)
        request.enqueue(object : Callback<List<AttemptModelUser>> {
            override fun onResponse(call: Call<List<AttemptModelUser>>, response: Response<List<AttemptModelUser>>
            ) {
                if (response.isSuccessful){
                    list.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<List<AttemptModelUser>>, t: Throwable) {
                Log.i("error","error user attempts")
            }
        })
    }

}