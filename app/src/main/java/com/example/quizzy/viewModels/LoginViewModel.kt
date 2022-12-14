package com.example.quizzy.viewModels

import android.database.Observable
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizzy.dataModel.model.LoginModel
import com.example.quizzy.Service.RetrofitBuilder
import com.example.quizzy.Service.userService
import com.example.quizzy.utilities.LoginStateSharedPrefrence
import com.example.quizzy.utilities.UserDetailsSharedPrefrence
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel : ViewModel() {


    val emailID = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val name = MutableLiveData<String>()

    private val _errorMessageEmail = MutableLiveData<String>()
    val errorMessageEmail: LiveData<String> get() = _errorMessageEmail

    private val _errorMessagePassword = MutableLiveData<String>()
    val errorMessagePassword: LiveData<String> get() = _errorMessagePassword

    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> get() = _loginStatus

    private val _loginResponse = MutableLiveData<String>()
    val loginResponse: LiveData<String> get() = _loginResponse



    private val service = RetrofitBuilder.buildService(userService::class.java)

    fun onLoginClick() {
        if (!emailID.value.isNullOrEmpty() && !password.value.isNullOrEmpty()) {
            val request = service.loginUser(emailID.value.toString(), password.value.toString())
            request.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {

                    if (response.isSuccessful) {

                        if ((response.body()?.substring(0,13)).equals("Login Success", true)){
                            _loginResponse.value= response.body()!!.substring(15)
                            _loginStatus.value = true
                        }
                        else {
                            if (response.body().toString().substring(0, 1).equals("p", true))
                                _errorMessagePassword.value = response.body().toString()
                            else _errorMessageEmail.value = response.body().toString()
                        }
                    }

                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.i(
                        "message",
                        "Failed :${emailID.value.toString()} ${password.value.toString()}, ${t.message}"
                    )
                }
            })
        }
        else { _errorMessageEmail.value="Email cannot be blank"
               _errorMessagePassword.value="Password cannot be null"}
    }

}