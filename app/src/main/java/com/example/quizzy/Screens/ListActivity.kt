package com.example.quizzy.Screens

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizzy.Adapter.HomePageAdapter
import com.example.quizzy.R
import com.example.quizzy.databinding.ActivityListBinding
import com.example.quizzy.viewModels.ListViewModel
import com.example.quizzy.viewModels.ProfileViewModel
import com.example.quizzy.viewModels.ViewModelFactory.ListViewModelFactory
import com.example.quizzy.viewModels.ViewModelFactory.ProfileViewModelFactory

class ListActivity : AppCompatActivity() {

    private lateinit var binding:ActivityListBinding
    private lateinit var adapter:HomePageAdapter
    private lateinit var viewModelFactory: ListViewModelFactory
    private lateinit var viewModel: ListViewModel
    private lateinit var key:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_list)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        key= intent.getStringExtra("key").toString()

        initViewModel()
        initRecycler()
        observers()
    }

    private fun initViewModel(){
        viewModelFactory = ListViewModelFactory(key)
        viewModel = ViewModelProvider(this, viewModelFactory)[ListViewModel::class.java]
        binding.listViewModel = viewModel
        binding.lifecycleOwner=this
    }

    private fun initRecycler(){
        binding.recyclerQuiz.layoutManager=LinearLayoutManager(this)
        adapter= HomePageAdapter(this,key)
        binding.recyclerQuiz.adapter=adapter
    }

    private fun observers(){

        viewModel.getList().observe(this){
            if (it!=null){
                adapter.setQuizList(it)
                adapter.notifyDataSetChanged()
            }
            else{
                Log.i("listActivity","Error in fetching list")
            }
        }
    }
}