package com.example.quizzy.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizzy.Service.QuizService
import com.example.quizzy.Service.RetrofitBuilder
import com.example.quizzy.dataModel.entity.Quiz
import com.example.quizzy.dataModel.extras.Questions
import com.example.quizzy.dataModel.extras.questionFormat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class QuizDetailViewModel(private val quizId: String) : ViewModel() {

    private val quizService = RetrofitBuilder.buildService(QuizService::class.java)

    private var _title = MutableLiveData<String>()
    private var _desc = MutableLiveData<String>()
    private var _time = MutableLiveData<String>()
    private var _onCorrect = MutableLiveData<String>()
    private var _onWrong = MutableLiveData<String>()
    private var _passing = MutableLiveData<String>()
    private var _maxScore = MutableLiveData<String>()
    private var _noOfQuestion = MutableLiveData<String>()

    val title :LiveData<String> get() = _title
    val desc :LiveData<String> get() = _desc
    val time :LiveData<String> get() = _time
    val onCorrect :LiveData<String> get() = _onCorrect
    val onWrong :LiveData<String> get() = _onWrong
    val passing :LiveData<String> get() = _passing
    val maxScore :LiveData<String> get() = _maxScore
    val noOfQuestion :LiveData<String> get() = _noOfQuestion


    private var _shouldStart=MutableLiveData<Boolean>()
    val shouldStart:LiveData<Boolean> get() = _shouldStart

    fun getData() {
        val request = quizService.fetchQuizById(quizId = quizId)
        request.enqueue(object : Callback<Quiz> {
            override fun onResponse(call: Call<Quiz>, response: Response<Quiz>) {

                if (response.isSuccessful) {

                    val quiz: Quiz? =response.body()
                    if (quiz != null) {
                        setViews(quiz)
                    }
                }
            }

            override fun onFailure(call: Call<Quiz>, t: Throwable) {
                Log.i("message", "Details not fetched")

            }
        })
    }

    fun setViews(quiz: Quiz){
        _title.value=quiz.title
        _desc.value=quiz.description
        _time.value=quiz.time.toString()+" min"

        val tempScore = quiz.questions?.score
        _onCorrect.value= tempScore?.onCorrect?.toInt().toString()
        _onWrong.value= "-"+tempScore?.onWrong?.toInt().toString()
        _passing.value= tempScore?.passingScore?.toInt().toString()
        _maxScore.value= tempScore?.maxScore?.toInt().toString()

        val a: Double? = tempScore?.onCorrect
        val b: Double? = tempScore?.maxScore
        val c= a?.let { b?.div(it) }
        _noOfQuestion.value= c?.toInt().toString()
    }

    fun startQuiz(){
        _shouldStart.value=true
    }
}