package com.example.quizzy.Screens

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.quizzy.R
import com.example.quizzy.viewModels.LoginViewModel
import com.example.quizzy.databinding.ActivityLoginBinding
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding;
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        viewModel=ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.loginViewModel = viewModel
        binding.lifecycleOwner = this


        viewModel.errorMessageEmail.observe(this, Observer { text_email.error = viewModel.errorMessageEmail.value })
        viewModel.errorMessagePassword.observe(this) { text_password.error = viewModel.errorMessagePassword.value }
        viewModel.loginStatus.observe(this) { isLogined ->
            if (isLogined) startActivity(Intent(this, SignUpActivity::class.java))
        }
    }


}