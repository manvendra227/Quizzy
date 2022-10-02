package com.example.quizzy.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizzy.Service.AttemptService
import com.example.quizzy.Service.RetrofitBuilder
import com.example.quizzy.Service.userService
import com.example.quizzy.dataModel.entity.User
import com.example.quizzy.dataModel.model.AttemptModelUser
import com.example.quizzy.dataModel.model.PasswordModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
    private var _emailID=MutableLiveData<String>()

    val username: LiveData<String> get() = _username
    val info: LiveData<String> get() = _info
    val solvedQuestions: LiveData<Int> get() = _solvedQuestions
    val accuracy: LiveData<String> get() = _accuracy
    val activeDays: LiveData<Int> get() = _activeDays
    val emailID:LiveData<String> get() =_emailID

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
                    if (solvedQuestions.value == 0) {
                        _accuracy.value = "0.00%"
                    } else {
                        val temp =
                            ((user.userPersonal.questionsCorrect.toDouble()) / (user.userPersonal.questionsSolved) * 100)
                        _accuracy.value = df.format(temp) + "%"
                    }
                    _activeDays.value = difference_In_Days.toInt()
                    _emailID.value=user.emailID
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.i("error", "error user by ID")
            }
        })
    }

    private fun fetchUserAttempts() {
        viewModelScope.launch(Dispatchers.IO){
            val response=attemptService.fetchUserAttempt(userId)
            if (response.isSuccessful) list.postValue(response.body())
            else   Log.i("error", "error user attempts")
        }
    }

    val deletePassword = MutableLiveData<String>()
    val deleteStatus=MutableLiveData<Boolean>()
    private val _errorMessageDeleteUser = MutableLiveData<String>()
    val errorMessageDeleteUser: LiveData<String> get() = _errorMessageDeleteUser

    fun deleteUser(){
        viewModelScope.launch(Dispatchers.IO) {
            val response=userService.deleteUser(userId, deletePassword.value.toString())
            if (response.isSuccessful){
                if (response.body().equals("Delete Success",true)){
                    deleteStatus.postValue(true)
                }else{
                    _errorMessageDeleteUser.postValue("Password is wrong")
                }
            }else{
                deleteStatus.postValue(false)
            }
        }
    }

    val changePasswordOld=MutableLiveData<String>()
    val changePasswordNew=MutableLiveData<String>()
    val changePasswordStatus=MutableLiveData<Boolean>()
    private val _errorMessageChangePasswordNew = MutableLiveData<String>()
    val errorMessageChangePasswordNew: LiveData<String> get() = _errorMessageChangePasswordNew
    private val _errorMessageChangePasswordOld = MutableLiveData<String>()
    val errorMessageChangePasswordOld: LiveData<String> get() = _errorMessageChangePasswordOld

    fun changePassword(){
        val passwordModel=PasswordModel(emailID.value.toString(), changePasswordNew.value.toString(), changePasswordOld.value.toString())
        viewModelScope.launch(Dispatchers.IO) {
            val response=userService.changePassword(passwordModel)
            if (response.isSuccessful){
                if (response.body().equals("Invalid Old Password")){
                    changePasswordStatus.postValue(false)
                    _errorMessageChangePasswordOld.postValue(response.body())
                }
                if (response.body().equals("New password saved successfully")){
                    changePasswordStatus.postValue(true)
                }
            }else{
                changePasswordStatus.postValue(false)
            }
        }
    }


}