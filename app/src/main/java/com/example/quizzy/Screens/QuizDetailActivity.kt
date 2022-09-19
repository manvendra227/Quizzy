package com.example.quizzy.Screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.quizzy.R
import com.example.quizzy.databinding.ActivityQuizDetailBinding
import com.example.quizzy.utilities.MyToast
import com.example.quizzy.viewModels.QuizDetailViewModel
import com.example.quizzy.viewModels.ViewModelFactory.QuizDetailsViewModelFactory
import kotlinx.android.synthetic.main.activity_quiz_detail.*

class QuizDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizDetailBinding
    private val toast=MyToast(this)
    private lateinit var viewModel: QuizDetailViewModel
    private lateinit var viewModelFactory: QuizDetailsViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_quiz_detail)

        val quizId=intent.getStringExtra("quizId")
        initViewModel(quizId!!)
        viewModel.getData()

        startQuiz()

    }

    fun initViewModel(quizId:String){

        viewModelFactory = QuizDetailsViewModelFactory(quizId)
        viewModel = ViewModelProvider(this, viewModelFactory)[QuizDetailViewModel::class.java]
        binding.quizDetailViewModel=viewModel
        binding.lifecycleOwner=this
    }

    fun startQuiz(){
        viewModel.shouldStart.observe(this, Observer { check->
            if(check){
                startActivity(Intent(this,QuizActivity::class.java))
                finish()
            }
        })
    }


}