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
import com.example.quizzy.Adapter.SearchTagAdapter
import com.example.quizzy.R
import com.example.quizzy.databinding.ActivityHomeBinding
import com.example.quizzy.utilities.MyToast
import com.example.quizzy.utilities.UserDetailsSharedPrefrence
import com.example.quizzy.viewModels.HomeViewModel
import com.example.quizzy.viewModels.QuizDetailViewModel
import com.example.quizzy.viewModels.ViewModelFactory.HomeViewModelFactory
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.layout_create_quiz.*

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var viewModelFactory: HomeViewModelFactory
    private lateinit var homePageAdapter: HomePageAdapter
    private lateinit var searchTagAdapter: SearchTagAdapter
    private val emailSP = UserDetailsSharedPrefrence()
    private var toast = MyToast(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        initRecyclerViewMain()
        initRecyclerViewTags()
        initViewModel()
    }

    private fun initRecyclerViewMain() {
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recycler_quiz.layoutManager = GridLayoutManager(this, 2)
        } else {
            recycler_quiz.layoutManager = LinearLayoutManager(this)
        }
        homePageAdapter = HomePageAdapter(this)
        recycler_quiz.adapter = homePageAdapter
    }

    private fun initRecyclerViewTags() {
        recycler_tags.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        searchTagAdapter = SearchTagAdapter(this)
        recycler_tags.adapter = searchTagAdapter
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun initViewModel() {
        viewModelFactory = HomeViewModelFactory(emailSP.getEmailID(this).toString())
        viewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
        binding.homeModel=viewModel

        viewModel.getMainList().observe(this, Observer {
            if (it != null) {
                homePageAdapter.setQuizList(it)
                homePageAdapter.notifyDataSetChanged()
            } else {
                toast.showLong("Error")
            }
        })
        viewModel.getQuizData()


        viewModel.getTagsList().observe(this, Observer{
            if (it !=null){
                searchTagAdapter.setTagList(it)
                searchTagAdapter.notifyDataSetChanged()
            }else{
                toast.showLong("No tags found error")
            }
        })
        viewModel.getTagsData()
    }
}