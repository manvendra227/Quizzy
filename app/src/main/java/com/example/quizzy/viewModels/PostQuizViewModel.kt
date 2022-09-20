package com.example.quizzy.viewModels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizzy.dataModel.enums.Difficulty
import com.example.quizzy.dataModel.enums.Gender
import com.example.quizzy.dataModel.enums.QuizType
import com.example.quizzy.dataModel.enums.Status
import com.example.quizzy.dataModel.extras.questionFormat

class PostQuizViewModel : ViewModel() {

    //Create Quiz Members
    val title = MutableLiveData<String>()
    val desc = MutableLiveData<String>()
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

    val indexTitle=MutableLiveData("Enter question number : 1")

    private var questionList= arrayListOf<questionFormat>()

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

    fun generateTags() {
        val str = tags.value
        val list = str?.split(" ", ",", " ,", ", ", ignoreCase = true)
        Log.i("message", list.toString())
    }

    fun checkStatus() {
        if (title.value.isNullOrEmpty()) {
            _errorTitle.value = "Quiz must have a Title"
        } else if (title.value!!.length <= 10) {
            _errorTitle.value = "Title is too small"
        } else if (tags.value.isNullOrEmpty()) {
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

    fun addQuesToList(){
        if (question.value.isNullOrEmpty()){ _errorQues.value="Question cannot be blank"}
        else if (optionA.value.isNullOrEmpty()){_errorA.value="Option A cannot be empty"}
        else if (optionB.value.isNullOrEmpty()){_errorB.value="Option B cannot be empty"}
        else if (optionC.value.isNullOrEmpty()){_errorC.value="Option C cannot be empty"}
        else if (optionD.value.isNullOrEmpty()){_errorD.value="Option D cannot be empty"}
        else if (answer.value!! !in 0..4){_errorAns.value="Choose Answer"}
        else{
            //Perform saving
            val options= listOf(optionA.value!!,optionB.value!!,optionC.value!!,optionD.value!!)
            val ques=questionFormat(question.value!!,options, answer.value!!,explanation.value.toString())
            questionList.add(ques)
            resetPage()
            Log.i("mess","Ques added ${questionList.toString()}")
        }
    }

    private fun resetPage(){
        question.value=""
        optionA.value=""
        optionB.value=""
        optionC.value=""
        optionD.value=""
        answer.value=69
        explanation.value=""
        _errorQues.value=null
        _errorA.value=null
        _errorB.value=null
        _errorC.value=null
        _errorD.value=null
        isA.value=false
        isB.value=false
        isC.value=false
        isD.value=false
        indexTitle.value="Enter question number : ${questionList.size+1}"
    }
}