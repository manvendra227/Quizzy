package com.example.quizzy.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizzy.Service.QuizService
import com.example.quizzy.Service.RetrofitBuilder
import com.example.quizzy.Service.userService
import com.example.quizzy.dataModel.model.QuizShortModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeViewModel(val userId: String) : ViewModel() {

    private var quizList: MutableLiveData<List<QuizShortModel>?> = MutableLiveData()
    private var tagList: MutableLiveData<List<String>?> = MutableLiveData()

    private val quizService = RetrofitBuilder.buildService(QuizService::class.java)
    private val userService = RetrofitBuilder.buildService(userService::class.java)

    val searchKey = MutableLiveData<String>()

    init {
        getQuizData()
        getTagsData()
    }

    fun getMainList(): MutableLiveData<List<QuizShortModel>?> {
        return quizList
    }

    fun getSearchList(): MutableLiveData<List<QuizShortModel>?> {
        return quizList
    }

    fun getTagsList(): MutableLiveData<List<String>?> {
        return tagList
    }

    fun getQuizData() {

       viewModelScope.launch(Dispatchers.IO) {
            val result = quizService.loadQuiz(0)
            if (result.isSuccessful) {
                quizList.postValue(result.body())
                Log.i("message", "Success in main list")
            } else {
                quizList.postValue(null)
                Log.i("message", "Failed main list")
            }
        }
    }

    fun getTagsData() {
       viewModelScope.launch(Dispatchers.IO) {
            val response = userService.fetchSearchTags(userId)
            if (response.isSuccessful) {
                tagList.postValue(response.body())
                Log.i("message", "Success in tag list")
            } else {
                quizList.postValue(null)
                Log.i("message", "Failed tag list")
            }
        }
    }

    fun getSearchData() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = quizService.loadSearch(searchKey.value ?: "")
            if (response.isSuccessful) quizList.postValue(response.body())
            else quizList.postValue(null)
        }
        viewModelScope.launch(Dispatchers.IO) {
            val response=userService.updateSearchTags(userId,searchKey.value?:"")
            if (response.isSuccessful) tagList.postValue(response.body())
            else tagList.postValue(null)
        }
    }

    fun getSearchDataByTag(tag: String) {

       viewModelScope.launch(Dispatchers.IO) {
            val response = quizService.loadSearch(tag)
            if (response.isSuccessful) quizList.postValue(response.body())
            else quizList.postValue(null)
        }
    }
}