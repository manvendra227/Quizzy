package com.example.quizzy.Screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.quizzy.R
import com.example.quizzy.databinding.ActivityQuizDetailBinding
import com.example.quizzy.utilities.MyToast
import com.example.quizzy.viewModels.QuizDetailViewModel
import com.example.quizzy.viewModels.QuizDetailsViewModelFactory
import com.example.quizzy.viewModels.QuizViewModel

class QuizDetailActivity : AppCompatActivity() {

    private lateinit var binding:ActivityQuizDetailBinding
    private val toast=MyToast(this)
    private lateinit var viewModel: QuizDetailViewModel
    private lateinit var viewModelFactory: QuizDetailsViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_quiz_detail)
        val quizId=intent.getStringExtra("quizId")
        initViewModel(quizId!!)
    }

    fun initViewModel(quizId:String){
        viewModelFactory = QuizDetailsViewModelFactory("abcd")
        viewModel = ViewModelProvider(this, viewModelFactory)[QuizDetailViewModel::class.java]
        binding.quizDetailViewModel=viewModel
        toast.showLong(quizId)
    }

}