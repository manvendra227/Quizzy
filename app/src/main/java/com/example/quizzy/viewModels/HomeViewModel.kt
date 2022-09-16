package com.example.quizzy.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizzy.Service.QuizService
import com.example.quizzy.Service.RetrofitBuilder
import com.example.quizzy.Service.userService
import com.example.quizzy.dataModel.model.QuizShortModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private lateinit var quizList: MutableLiveData<List<QuizShortModel>>
    val service = RetrofitBuilder.buildService(QuizService::class.java)


    init {
        quizList = MutableLiveData()
    }

    fun getLiveDataObserver(): MutableLiveData<List<QuizShortModel>> {
        return quizList
    }

    fun getData() {
       val request=service.loadQuiz()
        request.enqueue(object :Callback<List<QuizShortModel>>{

            override fun onResponse(call: Call<List<QuizShortModel>>, response: Response<List<QuizShortModel>>) {
                quizList.postValue(response.body())
                Log.i("message","Success")
            }

            override fun onFailure(call: Call<List<QuizShortModel>>, t: Throwable) {
              quizList.postValue(null)
                Log.i("message","Failed")
            }
        })
    }

}