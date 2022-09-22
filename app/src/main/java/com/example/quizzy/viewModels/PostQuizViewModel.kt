package com.example.quizzy.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.quizzy.Service.QuizService
import com.example.quizzy.Service.RetrofitBuilder
import com.example.quizzy.dataModel.entity.Quiz
import com.example.quizzy.dataModel.enums.Difficulty
import com.example.quizzy.dataModel.enums.QuizType
import com.example.quizzy.dataModel.extras.Questions
import com.example.quizzy.dataModel.extras.Score
import com.example.quizzy.dataModel.extras.questionFormat
import com.example.quizzy.dataModel.model.SavedUserModel
import com.example.quizzy.utilities.UserDetailsSharedPrefrence
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostQuizViewModel(application: Application) : AndroidViewModel(application) {

    private var userDetails = UserDetailsSharedPrefrence()

    //Create Quiz Members
    val title = MutableLiveData<String>()
    val desc = MutableLiveData("")
    val tags = MutableLiveData<String>()
    val time = MutableLiveData<String>()

    var onCorrect = MutableLiveData("10")
    val onWrong = MutableLiveData("-2")
    val passing = MutableLiveData("30")

    private var difficulty = MutableLiveData(Difficulty.EASY)
    private var quizType = MutableLiveData(QuizType.MCQ)
    private var timer = MutableLiveData(60)

    val isTimed = MutableLiveData(false)
    val isImported = MutableLiveData(false)
    val isEasy = MutableLiveData(false)
    val isMedium = MutableLiveData(false)
    val isHard = MutableLiveData(false)

    private val _errorTitle = MutableLiveData<String?>()
    val errorTitle: LiveData<String?> get() = _errorTitle

    private val _errorTags = MutableLiveData<String?>()
    val errorTags: LiveData<String?> get() = _errorTags

    private val _checkPass = MutableLiveData(false)
    val checkPass: LiveData<Boolean> get() = _checkPass

    private val _isUploaded = MutableLiveData(false)
    val isUploaded: LiveData<Boolean> get() = _isUploaded

    private val _quizId = MutableLiveData<String?>()
    val quizId: LiveData<String?> get() = _quizId

    //Add Questions Member
    val question = MutableLiveData<String>()
    val optionA = MutableLiveData<String?>()
    val optionB = MutableLiveData<String?>()
    val optionC = MutableLiveData<String?>()
    val optionD = MutableLiveData<String?>()
    val explanation = MutableLiveData("")

    val isA = MutableLiveData(false)
    val isB = MutableLiveData(false)
    val isC = MutableLiveData(false)
    val isD = MutableLiveData(false)

    private var answer = MutableLiveData(69)

    private val _errorA = MutableLiveData<String?>()
    private val _errorB = MutableLiveData<String?>()
    private val _errorC = MutableLiveData<String?>()
    private val _errorD = MutableLiveData<String?>()
    private val _errorQues = MutableLiveData<String?>()
    private val _errorAns = MutableLiveData<String?>()
    val errorA: LiveData<String?> get() = _errorA
    val errorB: LiveData<String?> get() = _errorB
    val errorC: LiveData<String?> get() = _errorC
    val errorD: LiveData<String?> get() = _errorD
    val errorQues: LiveData<String?> get() = _errorQues
    val errorAns: LiveData<String?> get() = _errorAns

    val indexTitle = MutableLiveData("Enter question number : 1")

    private var questionList: MutableLiveData<List<questionFormat>?> = MutableLiveData()
    private val quizService = RetrofitBuilder.buildService(QuizService::class.java)

    fun getQuestionList(): MutableLiveData<List<questionFormat>?> {
        return questionList
    }

    fun updateQuestionList(index:Int){
        val tempList= questionList.value?.reversed()?.toMutableList()
        tempList?.removeAt(index)
        questionList.value= tempList?.reversed()?.toList()
        indexTitle.value = "Enter question number : ${questionList.value!!.size + 1}"
    }

    fun setDifficulty() {
        if (isEasy.value == true) difficulty.value = Difficulty.EASY
        if (isMedium.value == true) difficulty.value = Difficulty.MEDIUM
        if (isHard.value == true) difficulty.value = Difficulty.HARD
    }

    fun onTimerClick() {
        Log.i("timer", timer.value.toString())
        if (isTimed.value == true && time.value != null) timer.value = time.value?.toInt()
        else if (isTimed.value == true && time.value == null) timer.value = 60
        else timer.value = 0
        Log.i("timer", timer.value.toString())
    }

    fun onIncrementClick(i: Int) {
        when (i) {
            1 -> onCorrect.value = onCorrect.value?.toInt()?.plus(1).toString()
            2 -> onWrong.value = onWrong.value?.toInt()?.plus(1).toString()
            3 -> passing.value = passing.value?.toDouble()?.plus(1).toString()
        }
    }

    fun onDecrementClick(i: Int) {
        when (i) {
            1 -> onCorrect.value = onCorrect.value?.toInt()?.minus(1).toString()
            2 -> onWrong.value = onWrong.value?.toInt()?.minus(1).toString()
            3 -> passing.value = passing.value?.toDouble()?.minus(1).toString()
        }
    }

    private fun generateTags(): List<String>? {
        val str = tags.value
        return str?.split(",", " ,", ", ", ignoreCase = true)
    }

    fun checkStatus() {
        if (title.value.isNullOrBlank()) {
            _errorTitle.value = "Quiz must have a Title"
        } else if (title.value!!.length <= 5) {
            _errorTitle.value = "Title is too small"
        } else if (tags.value.isNullOrBlank()) {
            _errorTags.value = "Tags make search easier, it would be helpful if you enter some tags"
        } else {
            _checkPass.value = true
        }
    }

    fun resetStatus() {
        _checkPass.value = false
        _errorTitle.value = null
        _errorTags.value = null
    }

    fun setAnswer() {
        if (isA.value == true) answer.value = 0
        if (isB.value == true) answer.value = 1
        if (isC.value == true) answer.value = 2
        if (isD.value == true) answer.value = 3
    }

    fun addQuesToList() {
        if (question.value.isNullOrBlank()) {
            _errorQues.value = "Question cannot be blank"
        } else if (optionA.value.isNullOrBlank()) {
            _errorA.value = "Option A cannot be empty"
        } else if (optionB.value.isNullOrBlank()) {
            _errorB.value = "Option B cannot be empty"
        } else if (optionC.value.isNullOrBlank()) {
            _errorC.value = "Option C cannot be empty"
        } else if (optionD.value.isNullOrBlank()) {
            _errorD.value = "Option D cannot be empty"
        } else if (answer.value!! !in 0..4) {
            _errorAns.value = "Choose Answer"
        } else {
            //Perform saving
            val options = listOf(
                optionA.value!!.trim(),
                optionB.value!!.trim(),
                optionC.value!!.trim(),
                optionD.value!!.trim()
            )
            val ques = questionFormat(
                question.value!!.trim(),
                options,
                answer.value!!.toString().trim(),
                explanation.value.toString().trim()
            )

            val tempList = mutableListOf(ques)
            val alreadyStored = questionList.value
            alreadyStored?.let { tempList.addAll(it) }
            questionList.value = tempList

            Log.i("mess", "Ques added ${questionList.value.toString()}")
            resetPage()

        }
    }

    private fun resetPage() {
        question.value = ""
        optionA.value = ""
        optionB.value = ""
        optionC.value = ""
        optionD.value = ""
        answer.value = 69
        explanation.value = ""
        _errorQues.value = null
        _errorA.value = null
        _errorB.value = null
        _errorC.value = null
        _errorD.value = null
        isA.value = false
        isB.value = false
        isC.value = false
        isD.value = false
        indexTitle.value = "Enter question number : ${questionList.value!!.size + 1}"
    }

    fun postQuiz() {
        //extracting user info here
        val gson = Gson()
        val savedUserResponse = userDetails.getUserDetails(getApplication())
        val savedUserModel = gson.fromJson(savedUserResponse, SavedUserModel::class.java)

        Log.i("check", savedUserModel.toString())
        if (questionList.value != null && questionList.value!!.size > 1) {
            val score = Score(
                maxScore = (questionList.value!!.size) * (onCorrect.value!!.toInt()),
                passingScore = passing.value!!.toDouble(),
                onCorrect = onCorrect.value!!.toInt(),
                onWrong = onWrong.value!!.toInt()
            )
            val questions = Questions(
                noOfQuestions = questionList.value!!.size,
                question = questionList.value!!.reversed(),
                score = score
            )
            val quiz = Quiz(
                quizID = "",
                title = title.value.toString(),
                description = desc.value.toString(),
                difficulty = difficulty.value,
                quizType = quizType.value,
                questions = questions,
                authorID = savedUserModel.userId,
                authorName = savedUserModel.name,
                tags = generateTags(),
                time = timer.value!!,
                timestamp = null,
                isImported = isImported.value!!,
                timesPlayed = 0,
                avgRating = 0.0
            )

            val request = quizService.saveQuiz(quiz)
            request.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        _quizId.value = response.body()
                        _isUploaded.value = true
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.i("Failed", t.message.toString())

                }
            })
        } else {
            Log.i("message", "Error in upload")
        }
    }

}