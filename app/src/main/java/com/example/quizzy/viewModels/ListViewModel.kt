package com.example.quizzy.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizzy.Service.QuizService
import com.example.quizzy.Service.RetrofitBuilder
import com.example.quizzy.dataModel.model.QuizShortModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListViewModel(val string: String) : ViewModel() {

    private val quizService = RetrofitBuilder.buildService(QuizService::class.java)
    private var list: MutableLiveData<List<QuizShortModel>?> = MutableLiveData()

    init {
        fetchUploads()
    }


    fun getList(): MutableLiveData<List<QuizShortModel>?> {
        return list
    }

    private fun fetchUploads() {
        Log.i("tag",string)
        val request = quizService.fetchUserUploads(string)
        request.enqueue(object : Callback<List<QuizShortModel>> {
            override fun onResponse(
                call: Call<List<QuizShortModel>>,
                response: Response<List<QuizShortModel>>
            ) {
                if (response.isSuccessful) {
                    list.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<List<QuizShortModel>>, t: Throwable) {
                Log.i("listViewModel", "error in fetching data")
            }
        })
    }

}