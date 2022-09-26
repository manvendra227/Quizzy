package com.example.quizzy.Screens

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizzy.Adapter.ReviewQuizAdapter
import com.example.quizzy.R
import com.example.quizzy.dataModel.extras.questionFormat
import com.example.quizzy.databinding.ActivityReviewBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_review.*
import java.lang.reflect.Type


class ReviewActivity : AppCompatActivity() {

    private lateinit var adapter: ReviewQuizAdapter
    private lateinit var binding: ActivityReviewBinding
    private lateinit var quesList:List<questionFormat>
    private lateinit var ansList:List<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_review)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        /**
         * Refrence-https://stackoverflow.com/questions/38618863/passing-arraylist-of-custom-object-to-another-activity
         */

        val bundle = intent.extras
        val jsonString1 = bundle!!.getString("QUESTION_LIST")
        val listString1: Type = object : TypeToken<List<questionFormat>>() {}.type
        quesList =Gson().fromJson(jsonString1,listString1)


        val jsonString2 = bundle.getString("ANSWER_LIST")
        val listString2: Type = object : TypeToken<List<Int>>() {}.type
        ansList =Gson().fromJson(jsonString2,listString2)

        initRecyclerView()

        close_button.setOnClickListener {
            finish()
        }
    }

    private fun initRecyclerView() {
        binding.reviewRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter= ReviewQuizAdapter(this)
        binding.reviewRecycler.adapter=adapter

        adapter.setReviewList(quesList)
        adapter.setUserAnswerList(ansList)
        adapter.notifyDataSetChanged()
    }

}