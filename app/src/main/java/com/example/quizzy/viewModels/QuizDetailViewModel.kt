package com.example.quizzy.viewModels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizzy.Service.AttemptService
import com.example.quizzy.Service.QuizService
import com.example.quizzy.Service.RetrofitBuilder
import com.example.quizzy.dataModel.entity.Quiz
import com.example.quizzy.dataModel.extras.Questions
import com.example.quizzy.dataModel.model.AttemptModelQuiz
import com.example.quizzy.dataModel.model.AttemptModelQuizUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.ZoneId

class QuizDetailViewModel(private val quizId: String, private val userId: String) : ViewModel() {

    private val quizService = RetrofitBuilder.buildService(QuizService::class.java)
    private val attemptService = RetrofitBuilder.buildService(AttemptService::class.java)


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

    val bullet1 = MutableLiveData("\u29ED Uploaded by username on date")
    val bullet2 = MutableLiveData("\u29ED Played XXXX times till date")
    val bullet3 = MutableLiveData("\u29ED Best score 78% by Manu")

    private var tagList: MutableLiveData<List<String>?> = MutableLiveData()
    private var quizAttemptList: MutableLiveData<List<AttemptModelQuiz>?> = MutableLiveData()
    private var userAttemptList: MutableLiveData<List<AttemptModelQuizUser>?> = MutableLiveData()

    var isTimed = MutableLiveData<Boolean>()

    init {
        getData()
        getQuizAttempt()
        getUserAttempts()
    }

    private var _quiz=MutableLiveData<Quiz?>()
    val quiz:LiveData<Quiz?> get() = _quiz

    fun getQuizData():LiveData<Quiz?>{
        return quiz
    }
    private fun getData() {

        val request = quizService.fetchQuizById(quizId = quizId)
        request.enqueue(object : Callback<Quiz> {
            override fun onResponse(call: Call<Quiz>, response: Response<Quiz>) {
                if (response.isSuccessful) {
                     val quiz: Quiz? = response.body()
                    _quiz.value=quiz
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun setViews(quiz: Quiz) {
        _title.value = quiz.title
        _desc.value = quiz.description

        if (quiz.time != 0) {
            _time.value = quiz.time.toString() + " min"
            isTimed.value = true
        } else {
            _time.value = "No Time limit"
            isTimed.value = false
        }

        val tempScore = quiz.questions?.score
        _onCorrect.value = tempScore?.onCorrect.toString()
        _onWrong.value = tempScore?.onWrong.toString()
        _passing.value = tempScore?.passingScore?.toString()
        _maxScore.value = tempScore?.maxScore.toString()
        _allQuestions.value = quiz.questions

        val x =quiz.timestamp
        val localDate: LocalDate = x?.toInstant()?.atZone(ZoneId.systemDefault())!!.toLocalDate()
        val year: Int = localDate.year
        val month: Int = localDate.monthValue
        val day: Int = localDate.dayOfMonth

        bullet1.value="\u29ED Uploaded by ${quiz.authorName} on ${day}/${month}/${year}"
        bullet2.value="\u29ED Played ${quiz.timesPlayed} times till date"

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

    @RequiresApi(Build.VERSION_CODES.N)
    fun getQuizAttemptList(): MutableLiveData<List<AttemptModelQuiz>?> {
        return quizAttemptList
    }

    fun getUserAttemptList(): MutableLiveData<List<AttemptModelQuizUser>?> {
        return userAttemptList
    }

    private fun getQuizAttempt() {
        viewModelScope.launch(Dispatchers.IO){
            val response=attemptService.fetchQuizAttempt(quizId)
            if (response.isSuccessful) quizAttemptList.postValue(response.body()!!)
            else Log.i("attempt", "failed quiz attempts")
        }
    }

    private fun getUserAttempts() {
        viewModelScope.launch(Dispatchers.IO) {
            val response=attemptService.fetchUserAttemptOnQuiz(quizId,userId)
            if (response.isSuccessful) userAttemptList.postValue(response.body()!!)
            else Log.i("attempt", "failed user on quiz attempts")
        }
    }

}