package com.example.quizzy.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizzy.Service.AttemptService
import com.example.quizzy.Service.QuizService
import com.example.quizzy.Service.RetrofitBuilder
import com.example.quizzy.dataModel.entity.Quiz
import com.example.quizzy.dataModel.extras.Questions
import com.example.quizzy.dataModel.model.AttemptModelQuiz
import com.example.quizzy.dataModel.model.AttemptModelQuizUser
import com.example.quizzy.dataModel.model.SavedUserModel
import com.example.quizzy.utilities.UserDetailsSharedPrefrence
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuizDetailViewModel(private val quizId: String,private val userId:String) : ViewModel() {

    private val quizService = RetrofitBuilder.buildService(QuizService::class.java)
    private val attemptService=RetrofitBuilder.buildService(AttemptService::class.java)


    private var _title = MutableLiveData<String>()
    private var _desc = MutableLiveData<String>()
    private var _time = MutableLiveData<String>()
    private var _onCorrect = MutableLiveData<String>()
    private var _onWrong = MutableLiveData<String>()
    private var _passing = MutableLiveData<String>()
    private var _maxScore = MutableLiveData<String>()
    private var _noOfQuestion = MutableLiveData<String>()
    private var _allQuestions = MutableLiveData<Questions?>()

    val title: LiveData<String> get() = _title
    val desc: LiveData<String> get() = _desc
    val time: LiveData<String> get() = _time
    val onCorrect: LiveData<String> get() = _onCorrect
    val onWrong: LiveData<String> get() = _onWrong
    val passing: LiveData<String> get() = _passing
    val maxScore: LiveData<String> get() = _maxScore
    val noOfQuestion: LiveData<String> get() = _noOfQuestion
    val allQuestions: LiveData<Questions?> get() = _allQuestions

    private var tagList: MutableLiveData<List<String>?> = MutableLiveData()
    private var quizAttemptList: MutableLiveData<List<AttemptModelQuiz>?> = MutableLiveData()
    private var userAttemptList: MutableLiveData<List<AttemptModelQuizUser>?> = MutableLiveData()

    var isTimed = MutableLiveData<Boolean>()


    fun getData() {
        val request = quizService.fetchQuizById(quizId = quizId)
        request.enqueue(object : Callback<Quiz> {
            override fun onResponse(call: Call<Quiz>, response: Response<Quiz>) {

                if (response.isSuccessful) {

                    val quiz: Quiz? = response.body()
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

    fun setViews(quiz: Quiz) {
        _title.value = quiz.title
        _desc.value = quiz.description

        if (quiz.time != 0) {
            _time.value = quiz.time.toString() + " min"
            isTimed.value=true
        } else {
            _time.value = "No Time limit"
            isTimed.value=false
        }

        val tempScore = quiz.questions?.score
        _onCorrect.value = tempScore?.onCorrect.toString()
        _onWrong.value = tempScore?.onWrong.toString()
        _passing.value = tempScore?.passingScore?.toString()
        _maxScore.value = tempScore?.maxScore.toString()
        _allQuestions.value = quiz.questions

        val a: Int? = tempScore?.onCorrect
        val b: Int? = tempScore?.maxScore
        var c = 0
        if (a != null && a != 0 && b != null) {
            c = b.div(a)
        }
        _noOfQuestion.value = c.toString()
        tagList.value = quiz.tags
    }



    fun getTagsList(): MutableLiveData<List<String>?> {
        return tagList
    }

    fun getQuizAttemptList():MutableLiveData<List<AttemptModelQuiz>?>{
        return quizAttemptList
    }

    fun getUserAttemptList():MutableLiveData<List<AttemptModelQuizUser>?>{
        return userAttemptList
    }

    init {
        getQuizAttempt()
        getUserAttempts()
    }
    private fun getQuizAttempt(){
        val request=attemptService.fetchQuizAttempt(quizId)
        request.enqueue(object :Callback<List<AttemptModelQuiz>?>{
            override fun onResponse(call: Call<List<AttemptModelQuiz>?>, response: Response<List<AttemptModelQuiz>?>) {
                if (response.isSuccessful){
                    quizAttemptList.postValue(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<AttemptModelQuiz>?>, t: Throwable) {
                Log.i("attempt","failed quiz attempts")
            }
        })
    }

    private fun getUserAttempts(){

        val request=attemptService.fetchUserAttemptOnQuiz(quizId,userId)
        request.enqueue(object :Callback<List<AttemptModelQuizUser>?>{
            override fun onResponse(call: Call<List<AttemptModelQuizUser>?>, response: Response<List<AttemptModelQuizUser>?>) {
                if (response.isSuccessful){
                    userAttemptList.postValue(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<AttemptModelQuizUser>?>, t: Throwable) {
                Log.i("attempt","failed user on quiz attempts")
            }
        })
    }

}