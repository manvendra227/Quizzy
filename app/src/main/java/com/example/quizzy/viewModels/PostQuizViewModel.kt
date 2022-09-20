package com.example.quizzy.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizzy.dataModel.enums.Difficulty
import com.example.quizzy.dataModel.enums.Gender
import com.example.quizzy.dataModel.enums.QuizType
import com.example.quizzy.dataModel.enums.Status

class PostQuizViewModel : ViewModel() {

    val title = MutableLiveData<String>()
    val desc = MutableLiveData<String>()
    val tags = MutableLiveData<String>()
    val time = MutableLiveData<String>()

    var onCorrect=MutableLiveData("10")
    val onWrong=MutableLiveData("-2")
    val passing=MutableLiveData("30")

    private var difficulty = MutableLiveData<Difficulty>()
    private var quizType = MutableLiveData(QuizType.MCQ)
    private var timer=MutableLiveData(60)

    val isTimed = MutableLiveData(false)
    val isImported = MutableLiveData(false)
    val isEasy = MutableLiveData(false)
    val isMedium = MutableLiveData(false)
    val isHard = MutableLiveData(false)


    private val _checkPass = MutableLiveData<String>()
    val checkPass: LiveData<String> get() = _checkPass


    fun onDifficultyClick() {
        if (isEasy.value == true) difficulty.value = Difficulty.EASY
        if (isMedium.value == true) difficulty.value = Difficulty.MEDIUM
        if (isHard.value == true) difficulty.value = Difficulty.HARD

        Log.i("Mess",difficulty.value.toString())
    }

    fun onTimerClick() {
        Log.i("timer",timer.value.toString())
        if (isTimed.value==true && time.value!=null) timer.value= time.value?.toInt()
        else if (isTimed.value==true && time.value==null) timer.value= 60
        else timer.value=0
        Log.i("timer",timer.value.toString())
    }

    fun onIncrementClick(i:Int){
        when(i){
            1-> onCorrect.value=onCorrect.value?.toInt()?.plus(1).toString()
            2-> onWrong.value=onWrong.value?.toInt()?.plus(1).toString()
            3-> passing.value=passing.value?.toDouble()?.plus(1).toString()
        }
    }
    fun onDecrementClick(i:Int){
        when(i){
            1-> onCorrect.value=onCorrect.value?.toInt()?.minus(1).toString()
            2-> onWrong.value=onWrong.value?.toInt()?.minus(1).toString()
            3-> passing.value=passing.value?.toDouble()?.minus(1).toString()
        }
    }

    fun generateTags(){
        val str = tags.value
        val list = str?.split(" ",","," ,",", ", ignoreCase = true)
        Log.i("message",list.toString())
    }

    fun checkStatus(){
    }

}