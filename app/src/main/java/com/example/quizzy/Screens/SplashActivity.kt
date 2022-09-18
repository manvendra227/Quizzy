package com.example.quizzy.Screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.quizzy.R
import com.example.quizzy.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_splash)
    }
}