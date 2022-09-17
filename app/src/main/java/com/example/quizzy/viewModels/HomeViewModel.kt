package com.example.quizzy.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizzy.Service.QuizService
import com.example.quizzy.Service.RetrofitBuilder
import com.example.quizzy.Service.userService
import com.example.quizzy.dataModel.model.QuizShortModel
import com.example.quizzy.utilities.LoginStateSharedPrefrence
import com.example.quizzy.utilities.UserDetailsSharedPrefrence
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(email:String) : ViewModel() {

    private var quizList: MutableLiveData<List<QuizShortModel>> = MutableLiveData()
    private var tagList : MutableLiveData<List<String>> = MutableLiveData()
    private val emailId=email

    val quizService = RetrofitBuilder.buildService(QuizService::class.java)
    val userService = RetrofitBuilder.buildService(userService::class.java)


    fun getMainList(): MutableLiveData<List<QuizShortModel>> {
        return quizList
    }

    fun getTagsList():MutableLiveData<List<String>>{
        return tagList
    }


    fun getQuizData() {
       val request=quizService.loadQuiz()
        request.enqueue(object :Callback<List<QuizShortModel>>{

            override fun onResponse(call: Call<List<QuizShortModel>>, response: Response<List<QuizShortModel>>) {
                quizList.postValue(response.body())
                Log.i("message","Success in main list")
            }

            override fun onFailure(call: Call<List<QuizShortModel>>, t: Throwable) {
              quizList.postValue(null)
                Log.i("message","Failed main list")
            }
        })
    }

    fun getTagsData(){
        val request=userService.fetchSearchTags(emailId);
        Log.i("email",emailId)
        request.enqueue(object :Callback<List<String>>{
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                tagList.postValue(response.body())
                Log.i("message","Success in tag list")

            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                quizList.postValue(null)
                Log.i("message","Failed tag list")
            }
        })
    }

}