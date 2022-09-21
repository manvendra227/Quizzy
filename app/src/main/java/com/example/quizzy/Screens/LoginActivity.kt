package com.example.quizzy.Screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.quizzy.R
import com.example.quizzy.databinding.ActivityLoginBinding
import com.example.quizzy.utilities.LoginStateSharedPrefrence
import com.example.quizzy.utilities.MyToast
import com.example.quizzy.utilities.UserDetailsSharedPrefrence
import com.example.quizzy.viewModels.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private var saveSPLogin=LoginStateSharedPrefrence()
    private var savedUser=UserDetailsSharedPrefrence()
    private var toast=MyToast(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        initViewModel()
        errors()

        viewModel.loginStatus.observe(this) { isLogined ->
            if (isLogined) {
                startActivity(Intent(this, HomeActivity::class.java))
                saveDetailsOnFirstLogin()
                finish()
            }
        }

        sign_up_button.setOnClickListener {  startActivity(Intent(this, SignUpActivity::class.java)) }
        forgotPassword.setOnClickListener { toast.showLong("Under making") }

        if (saveSPLogin.getState(this)){
            startActivity(Intent(this,HomeActivity::class.java))
            finish()
        }
    }

    private fun initViewModel(){
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.loginViewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun errors(){
        viewModel.errorMessageEmail.observe(this, Observer { text_email.error = viewModel.errorMessageEmail.value })
        viewModel.errorMessagePassword.observe(this) { text_password.error = viewModel.errorMessagePassword.value }
    }

    private fun saveDetailsOnFirstLogin(){

        val response: String? =viewModel.loginResponse.value
        val x:Int= response?.indexOf('|') ?: 0
        val userId= response?.substring(0,x)?.trim()
        val name= response?.substring(x+2)?.trim()
        savedUser.setUserDetails(this,viewModel.emailID.value!!.trim(), userId.toString(), name.toString())
        saveSPLogin.setState(this,true)
    }
}