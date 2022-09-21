package com.example.quizzy.Screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.example.quizzy.R
import com.example.quizzy.databinding.ActivityPostQuizBinding
import kotlinx.android.synthetic.main.activity_post_quiz.*
import kotlinx.android.synthetic.main.fragment_create_quiz.*
import kotlinx.android.synthetic.main.fragment_create_quiz.view.*

class PostQuizActivity : AppCompatActivity() {

    private lateinit var binding:ActivityPostQuizBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_post_quiz)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

}