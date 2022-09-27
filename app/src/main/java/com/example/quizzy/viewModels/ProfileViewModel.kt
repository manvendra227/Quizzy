package com.example.quizzy.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizzy.Service.AttemptService
import com.example.quizzy.Service.RetrofitBuilder
import com.example.quizzy.Service.userService
import com.example.quizzy.dataModel.entity.User
import com.example.quizzy.dataModel.model.AttemptModelUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.util.*

class ProfileViewModel(val userId: String) : ViewModel() {


    private val attemptService = RetrofitBuilder.buildService(AttemptService::class.java)
    private val userService = RetrofitBuilder.buildService(userService::class.java)
    private var list: MutableLiveData<List<AttemptModelUser>?> = MutableLiveData()
    private lateinit var user: User

    private var _username = MutableLiveData<String>()
    private var _info = MutableLiveData<String>()
    private var _solvedQuestions = MutableLiveData<Int>()
    private var _accuracy = MutableLiveData<String>()
    private var _activeDays = MutableLiveData<Int>()

    val username: LiveData<String> get() = _username
    val info: LiveData<String> get() = _info
    val solvedQuestions: LiveData<Int> get() = _solvedQuestions
    val accuracy: LiveData<String> get() = _accuracy
    val activeDays: LiveData<Int> get() = _activeDays

    init {
        fetchUser()
        fetchUserAttempts()
    }


    fun getUserAttemptList(): MutableLiveData<List<AttemptModelUser>?> {
        return list
    }

    private fun fetchUser() {
        val request = userService.getUserById(userId)
        request.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {

                val df: DecimalFormat = DecimalFormat("0.00")

                if (response.isSuccessful) {
                    user = response.body()!!
                    Log.i("user", user.toString())
                    val regTime = user.timestamp
                    val difference_In_Time: Long =
                        (Calendar.getInstance().time.time) - (regTime.time)

                    val difference_In_Years: Long = (difference_In_Time
                            / (1000L * 60 * 60 * 24 * 365))

                    val difference_In_Days: Long = ((difference_In_Time
                            / (1000 * 60 * 60 * 24))
                            % 365)

                    _username.value = user.name
                    _info.value = "${user.gender} | 20 years | ${user.status} "
                    _solvedQuestions.value = user.userPersonal.questionsSolved
                    Log.i("message", solvedQuestions.value.toString())
                    if (solvedQuestions.value == 0) {
                        _accuracy.value = "0.00%"
                    } else {
                        val temp =
                            ((user.userPersonal.questionsCorrect.toDouble()) / (user.userPersonal.questionsSolved) * 100)
                        _accuracy.value = df.format(temp) + "%"
                    }
                    _activeDays.value = difference_In_Days.toInt()
                    Log.i("message", accuracy.value.toString())
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.i("error", "error user by ID")
            }
        })
    }

    private fun fetchUserAttempts() {
        val request = attemptService.fetchUserAttempt(userId)
        request.enqueue(object : Callback<List<AttemptModelUser>> {
            override fun onResponse(
                call: Call<List<AttemptModelUser>>, response: Response<List<AttemptModelUser>>
            ) {
                if (response.isSuccessful) {
                    list.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<List<AttemptModelUser>>, t: Throwable) {
                Log.i("error", "error user attempts")
            }
        })
    }


}