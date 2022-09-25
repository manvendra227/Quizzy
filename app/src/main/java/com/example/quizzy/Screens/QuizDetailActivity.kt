package com.example.quizzy.Screens

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.quizzy.Adapter.QuizTagAdapter
import com.example.quizzy.R
import com.example.quizzy.databinding.ActivityQuizDetailBinding
import com.example.quizzy.utilities.MyToast
import com.example.quizzy.viewModels.QuizDetailViewModel
import com.example.quizzy.viewModels.ViewModelFactory.QuizDetailsViewModelFactory
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_quiz_detail.*


class QuizDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizDetailBinding
    private val toast=MyToast(this)
    private lateinit var viewModel: QuizDetailViewModel
    private lateinit var viewModelFactory: QuizDetailsViewModelFactory
    private lateinit var quizTagAdapter: QuizTagAdapter
    private lateinit var quizId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_quiz_detail)

        quizId= intent.getStringExtra("quizId").toString()
        initViewModel(quizId)
        viewModel.getData()

        initRecyclerView()
        loadTags()
        startQuiz()

    }

    private fun initViewModel(quizId:String){

        viewModelFactory = QuizDetailsViewModelFactory(quizId)
        viewModel = ViewModelProvider(this, viewModelFactory)[QuizDetailViewModel::class.java]
        binding.quizDetailViewModel=viewModel
        binding.lifecycleOwner=this
    }

    private fun initRecyclerView(){

        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW

        tag_recycler.layoutManager=layoutManager
        quizTagAdapter= QuizTagAdapter(this)
        tag_recycler.adapter=quizTagAdapter

    }

    private fun startQuiz(){
        viewModel.shouldStart.observe(this, Observer { check->
            if(check){
                startActivity(Intent(this,QuizActivity::class.java).putExtra("quizId",quizId).putExtra("time",viewModel.time.value))
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadTags(){

        viewModel.getTagsList().observe(this, Observer {
            if (it != null) {
                quizTagAdapter.setTagList(it)
                quizTagAdapter.notifyDataSetChanged()
            } else {
                toast.showLong("No tags found error")
            }
        })
    }
}