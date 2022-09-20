package com.example.quizzy.Screens

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.quizzy.R
import com.example.quizzy.databinding.ActivitySignUpBinding
import com.example.quizzy.utilities.MyToast
import com.example.quizzy.viewModels.SignUpViewModel
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel:SignUpViewModel
    private var toast=MyToast(this)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_sign_up)

        viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
        binding.signUpModel = viewModel
        binding.lifecycleOwner = this

        viewModel.errorMessageName.observe(this){name.error=viewModel.errorMessageName.value}
        viewModel.errorMessageEmail.observe(this){email.error=viewModel.errorMessageEmail.value}
        viewModel.errorMessagePassword.observe(this){password.error=viewModel.errorMessagePassword.value}
        viewModel.errorMessageMPassword.observe(this){password_confirm.error=viewModel.errorMessageMPassword.value}
        viewModel.userCreated.observe(this){userCreated ->
            if (userCreated)
                toast.showLong("Mail has been sent, verify to login")
        }
    }


}