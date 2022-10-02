package com.example.quizzy.viewModels

import android.annotation.SuppressLint
import android.os.Build
import android.os.CountDownTimer
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizzy.Service.AttemptService
import com.example.quizzy.Service.QuizService
import com.example.quizzy.Service.RetrofitBuilder
import com.example.quizzy.dataModel.entity.Quiz
import com.example.quizzy.dataModel.enums.BuzzType
import com.example.quizzy.dataModel.enums.Progress
import com.example.quizzy.dataModel.extras.Questions
import com.example.quizzy.dataModel.extras.Score
import com.example.quizzy.dataModel.extras.questionFormat
import com.example.quizzy.dataModel.model.AttemptSaveModel
import com.example.quizzy.dataModel.model.ProgressModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class QuizViewModel(val userId: String, val quizId: String, val time: String) : ViewModel() {


    companion object {

        // This is when the quiz is over
        private const val DONE = 0L

        // This is the time when the phone will start buzzing each second
        private const val COUNTDOWN_PANIC_SECONDS = 10L

        // This is the number of milliseconds in a second
        private const val ONE_SECOND = 1000L

    }

    //Countdown timer
    private var startTime: String
    private lateinit var endTime: String
    private val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS")
    private var quizService: QuizService
    private var attemptService = RetrofitBuilder.buildService(AttemptService::class.java)

    private var progressList: MutableLiveData<List<ProgressModel>?> = MutableLiveData()
    private var questionList = listOf<questionFormat>()

    private lateinit var timer: CountDownTimer
    private lateinit var allQuestions: Questions
    private lateinit var scores: Score
    private lateinit var tempAnswer: MutableList<Int>
    private lateinit var pins: MutableList<Int>

    val isPinned = MutableLiveData(false)
    val wantSubmit = MutableLiveData(false)
    val rating = MutableLiveData(0.0f)
    val isRated = MutableLiveData(false)


    private var _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long> get() = _currentTime

    //buzzer event
    private val _eventBuzz = MutableLiveData<BuzzType>()
    val eventBuzz: LiveData<BuzzType> get() = _eventBuzz

    //quiz finished
    private var _eventQuizFinished = MutableLiveData(false)
    val eventQuizFinished: LiveData<Boolean> get() = _eventQuizFinished

    //answer selection
    private var _selection = MutableLiveData(-1)
    val selection: LiveData<Int> get() = _selection

    private var _counter = MutableLiveData(0)
    val counter: LiveData<Int> get() = _counter

    private var _isPassed = MutableLiveData(false)
    val isPassed: LiveData<Boolean> get() = _isPassed

    private var _percentage = MutableLiveData("")
    val percentage: LiveData<String> get() = _percentage

    init {
        Log.i("Quiz", "QuizViewModel created!")
        startTime = sdf.format(Calendar.getInstance().time)
        quizService = RetrofitBuilder.buildService(QuizService::class.java)
        fetchQuestions()

        //timer
        if (!(time.isNullOrEmpty() || time.equals("No Time limit", true))) {

            _eventQuizFinished.value = false
            val quizTimeInMins = time.filter { it.isDigit() }
            val quizTimeInMilliSeconds = quizTimeInMins.toLong() * 1000L * 60L

            timer = object : CountDownTimer(quizTimeInMilliSeconds, ONE_SECOND) {

                override fun onTick(millisUntilFinished: Long) {
                    if (millisUntilFinished / ONE_SECOND <= COUNTDOWN_PANIC_SECONDS) {
                        _eventBuzz.value = BuzzType.COUNTDOWN_PANIC
                    }
                    _currentTime.value = millisUntilFinished / 1000
                }

                override fun onFinish() {
                    _currentTime.value = DONE
                    calculateScore()
                    saveAttempt()
                    _eventQuizFinished.value = true
                    _eventBuzz.value = BuzzType.QUIZ_OVER
                }
            }
            timer.start()
        }
    }


    private var _question = MutableLiveData<String>()
    private var _optionA = MutableLiveData<String>()
    private var _optionB = MutableLiveData<String>()
    private var _optionC = MutableLiveData<String>()
    private var _optionD = MutableLiveData<String>()
    private var _index = MutableLiveData(0)
    private var _noOfQuestions = MutableLiveData(0)

    val question: LiveData<String> get() = _question
    val optionA: LiveData<String> get() = _optionA
    val optionB: LiveData<String> get() = _optionB
    val optionC: LiveData<String> get() = _optionC
    val optionD: LiveData<String> get() = _optionD
    val index: LiveData<Int> get() = _index
    val noOfQuestions: LiveData<Int> get() = _noOfQuestions


    fun getProgressList(): MutableLiveData<List<ProgressModel>?> {
        return progressList
    }

    fun getQuestionList(): List<questionFormat> {
        return questionList
    }

    fun getAnswerList():List<Int>{
        return tempAnswer
    }

    private fun fetchQuestions() {
        val request = quizService.fetchQuestionsById(quizId = quizId)
        request.enqueue(object : Callback<Questions> {

            override fun onResponse(call: Call<Questions>, response: Response<Questions>) {
                if (response.isSuccessful && response.body() != null) {

                    allQuestions = response.body()!!
                    scores = response.body()!!.score!!
                    questionList = response.body()!!.question!!
                    _noOfQuestions.value = response.body()!!.noOfQuestions

                    tempAnswer = MutableList(noOfQuestions.value!!) { -1 }
                    pins = MutableList(noOfQuestions.value!!) { -1 }
                    Log.i("message", tempAnswer.toString())

                    progressList.value =
                        List(questionList.size) { ProgressModel(Progress.UNMARKED) }
                    if (questionList.isNotEmpty()) {
                        _index.value = 0
                        setQuestion(index.value)
                    }
                } else {
                    Log.i("message", "Response Null")
                }
            }

            override fun onFailure(call: Call<Questions>, t: Throwable) {
                Log.i("message", "Cannot fetch questions")
            }
        })
    }

    fun setQuestion(index: Int?) {
        resetSelections()
        if (!questionList.isNullOrEmpty()) {
            val currentQuestion = questionList[index!!]
            _question.value = "${index.plus(1)}.${currentQuestion.question}"
            _optionA.value = currentQuestion.options?.get(0)
            _optionB.value = currentQuestion.options?.get(1)
            _optionC.value = currentQuestion.options?.get(2)
            _optionD.value = currentQuestion.options?.get(3)
            _selection.value = tempAnswer[index]
            isPinned.value = pins[index] == 1
        }
    }

    fun selectQuestion(index: Int) {
        resetSelections()
        _index.value = index
    }

    private fun resetSelections() {
        _selection.value = -1
    }

    private fun saveAnswer(answer: Int) {
        index.value?.let { tempAnswer.set(it, answer) }
        if (pins[index.value!!] == 1) setProgress(Progress.PINNED) else setProgress(Progress.MARKED)
    }

    fun onClick(marker: Int) {
        resetSelections()
        when (marker) {
            0 -> {
                _selection.value = 0
            }
            1 -> {
                _selection.value = 1
            }
            2 -> {
                _selection.value = 2
            }
            3 -> {
                _selection.value = 3
            }
        }
        saveAnswer(marker)
    }

    fun prevClick() {
        _index.value = _index.value?.minus(1)
        _eventBuzz.value = BuzzType.NEXT
    }

    fun nextClick() {
        _index.value = _index.value?.plus(1)
        _eventBuzz.value = BuzzType.NEXT
    }

    fun submitClick() {
        _eventBuzz.value = BuzzType.NEXT
        _currentTime.value = DONE
        calculateScore()

        //if no timer , no cancel required
        if (!(time.isNullOrEmpty() || time.equals("No Time limit", true))) {
            timer.cancel()
        }
    }

    fun pinClick() {

        if (isPinned.value == false) {
            isPinned.value = true
            index.value?.let { pins.set(it, 1) }
            setProgress(Progress.PINNED)

        } else {
            isPinned.value = false
            index.value?.let { pins.set(it, -1) }
            if (tempAnswer[index.value!!] == -1) setProgress(Progress.UNMARKED) else setProgress(
                Progress.MARKED
            )
        }
    }

    private fun setProgress(p: Progress) {
        val tempList = progressList.value?.toMutableList()
        if (p == Progress.MARKED) tempList?.set(index.value!!, ProgressModel(Progress.MARKED))
        if (p == Progress.PINNED) tempList?.set(index.value!!, ProgressModel(Progress.PINNED))
        if (p == Progress.UNMARKED) tempList?.set(index.value!!, ProgressModel(Progress.UNMARKED))
        progressList.value = tempList?.toList()
    }

    private fun calculateScore() {

        endTime=sdf.format(Calendar.getInstance().time)
        for ((index, value) in questionList.withIndex()) {
            if (value.answer.toInt() == tempAnswer[index]) _counter.value = _counter.value?.plus(1)
        }

        val tempPer = counter.value?.toDouble()?.div(noOfQuestions.value!!.toDouble())?.times(100.0)
        _percentage.value = String.format("%.2f", tempPer)
        if (tempPer!! >= scores.passingScore) _isPassed.value = true
    }

    @SuppressLint("SimpleDateFormat")
    fun saveAttempt() {

        val attempt = AttemptSaveModel(
            userId = userId,
            quizId = quizId,
            score = percentage.value?.toDouble() ?: 0.0,
            startTime = startTime,
            endTime = endTime,
            feedback = rating.value?.toDouble() ?: 0.0,
            newQuestions = scores.maxScore.div(scores.onCorrect),
            newCorrect = counter.value?.toInt()
        )
        val request = attemptService.saveAttempt(attempt)
        request.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) Log.i("attempt", "response saved")
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.i("attempt", "failed")
            }
        })
    }

    fun onBuzzComplete() {
        _eventBuzz.value = BuzzType.NO_BUZZ
    }

    fun onRatingClick() {
        isRated.value = true
    }

    override fun onCleared() {
        super.onCleared()
        if (!(time.isNullOrEmpty() || time.equals("No Time limit", true))) {
            timer.cancel()
        }
        Log.i("Quiz", "QuizViewModel destroyed!")
    }
}