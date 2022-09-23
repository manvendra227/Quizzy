package com.example.quizzy.viewModels

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizzy.Service.QuizService
import com.example.quizzy.Service.RetrofitBuilder
import com.example.quizzy.dataModel.enums.BuzzType
import com.example.quizzy.dataModel.enums.Progress
import com.example.quizzy.dataModel.extras.Questions
import com.example.quizzy.dataModel.extras.Score
import com.example.quizzy.dataModel.extras.questionFormat
import com.example.quizzy.dataModel.model.ProgressModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuizViewModel(val quizId: String, val time: String) : ViewModel() {


    companion object {

        // This is when the quiz is over
        private const val DONE = 0L

        // This is the time when the phone will start buzzing each second
        private const val COUNTDOWN_PANIC_SECONDS = 10L

        // This is the number of milliseconds in a second
        private const val ONE_SECOND = 1000L

    }

    //Countdown timer
    private var timer: CountDownTimer

    private var _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long> get() = _currentTime

    private val _eventBuzz = MutableLiveData<BuzzType>()
    val eventBuzz: LiveData<BuzzType> get() = _eventBuzz

    //quiz finished
    private var _eventQuizFinished = MutableLiveData<Boolean>()
    val eventQuizFinished: LiveData<Boolean> get() = _eventQuizFinished

    init {
        Log.i("Quiz", "QuizViewModel created!")

        //fetchData
        fetchQuestions()

        //timer
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
                _eventQuizFinished.value = true
                _eventBuzz.value = BuzzType.QUIZ_OVER
            }
        }
        timer.start()
    }


    private lateinit var tempAnswer: MutableList<Int>
    private var progressList: MutableLiveData<List<ProgressModel>?> = MutableLiveData()
    private val quizService = RetrofitBuilder.buildService(QuizService::class.java)

    private lateinit var allQuestions: Questions
    private lateinit var scores: Score
    private var questionList = listOf<questionFormat>()

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

    private var _selection=MutableLiveData(-1)
    val selection:LiveData<Int> get() = _selection

    fun getProgressList(): MutableLiveData<List<ProgressModel>?> {
        return progressList
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

    fun selectQuestion(index: Int) {
        resetSelections()
        _index.value = index
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
            _selection.value=tempAnswer[index]
        }
    }

    private fun resetSelections() {
        _selection.value=-1
    }

    private fun saveAnswer(answer:Int) {
        index.value?.let { tempAnswer.set(it,answer) }
    }

    fun onClick(marker: Int){
        resetSelections()
        when(marker) {
            0->{_selection.value=0}
            1->{_selection.value=1}
            2->{_selection.value=2}
            3->{_selection.value=3}
        }
        saveAnswer(marker)
        Log.i("message",tempAnswer.toString())
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
    }

    fun onBuzzComplete() {
        _eventBuzz.value = BuzzType.NO_BUZZ
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
        Log.i("Quiz", "QuizViewModel destroyed!")
    }
}