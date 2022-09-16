package com.example.quizzy.Screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizzy.Adapter.HomePageAdapter
import com.example.quizzy.R
import com.example.quizzy.databinding.ActivityHomeBinding
import com.example.quizzy.utilities.MyToast
import com.example.quizzy.viewModels.HomeViewModel
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.layout_create_quiz.*

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var homePageAdapter: HomePageAdapter
    private var toast=MyToast(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        initRecyclerView()
        initViewModel()
    }

    private fun initRecyclerView(){
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recycler_quiz.layoutManager=GridLayoutManager(this,2)
        } else {
            recycler_quiz.layoutManager=LinearLayoutManager(this)
        }
        homePageAdapter= HomePageAdapter(this)
        recycler_quiz.adapter=homePageAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initViewModel(){
        viewModel=ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModel.getLiveDataObserver().observe(this, Observer {
            if (it !=null) {
                homePageAdapter.setQuizList(it)
                homePageAdapter.notifyDataSetChanged()
            }else{
                toast.showLong("Error")
            }
        })
        viewModel.getData()
    }
}