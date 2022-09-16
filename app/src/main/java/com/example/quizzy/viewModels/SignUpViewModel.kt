package com.example.quizzy.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizzy.Service.RetrofitBuilder
import com.example.quizzy.Service.userService
import com.example.quizzy.dataModel.enums.Gender
import com.example.quizzy.dataModel.enums.Status
import com.example.quizzy.dataModel.model.UserModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel:ViewModel() {

    val emailID = MutableLiveData<String>("")
    val name = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")
    val matchingPassword = MutableLiveData<String>("")
    val rawDate=MutableLiveData<String>("")
    private val gender=MutableLiveData<Gender>(Gender.MALE)
    private val status=MutableLiveData<Status>(Status.STUDENT)

    val isMale=MutableLiveData<Boolean>(false)
    val isStudent=MutableLiveData<Boolean>(false)

    private val _errorMessageName = MutableLiveData<String>()
    val errorMessageName: LiveData<String> get() = _errorMessageName

    private val _errorMessageEmail = MutableLiveData<String>()
    val errorMessageEmail: LiveData<String> get() = _errorMessageEmail

    private val _errorMessagePassword = MutableLiveData<String>()
    val errorMessagePassword: LiveData<String> get() = _errorMessagePassword

    private val _errorMessageMPassword = MutableLiveData<String>()
    val errorMessageMPassword: LiveData<String> get() = _errorMessageMPassword

    private val _userCreated=MutableLiveData<Boolean>(false)
    val userCreated:LiveData<Boolean> get()=_userCreated

    private val service = RetrofitBuilder.buildService(userService::class.java)

    fun onCreateProfile(){

            val userModel = UserModel(
                name = name.value!!,
                emailId = emailID.value!!,
                dob = rawDate.value!!,
                password = password.value!!,
                matchingPassword=matchingPassword.value!!,
                gender = gender.value!!,
                status = status.value!!
            )
        Log.i("message", userModel.toString())
            val request=service.saveUser(userModel)
            request.enqueue(object :Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {

                    if (response.isSuccessful){
                        Log.i("message","success ${response.body().toString()}")
                        if(response.body().toString().equals("Success",true)){
                           _userCreated.value=true
                        }
                        else{
                            if (response.body().toString().substring(0,1).equals("N",true)) _errorMessageName.value=response.body().toString()
                            else if (response.body().toString().substring(0,1).equals("P",true)) _errorMessagePassword.value=response.body().toString()
                            else if (response.body().toString().substring(0,1).equals("Password not same",true)) _errorMessageMPassword.value=response.body().toString()
                            else if (response.body().toString().substring(0,1).equals("E",true)) _errorMessageEmail.value=response.body().toString()
                            else _errorMessageEmail.value=response.body().toString()
                        }
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.i("message","Failed")
                }
            })
    }

    fun onGenderClick(){
        if (isMale.value == true) gender.value=Gender.MALE
        else gender.value=Gender.FEMALE
        Log.i("message","${gender.value}")
    }
    fun onStatusClick(){
        if(isStudent.value==true) status.value=Status.STUDENT
        else status.value=Status.WORKING
        Log.i("message","${status.value}")
    }
}