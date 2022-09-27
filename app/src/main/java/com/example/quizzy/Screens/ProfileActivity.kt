package com.example.quizzy.Screens

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizzy.Adapter.UserAttemptsAdapter
import com.example.quizzy.R
import com.example.quizzy.databinding.ActivityProfileBinding
import com.example.quizzy.viewModels.ProfileViewModel
import com.example.quizzy.viewModels.ViewModelFactory.ProfileViewModelFactory

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding:ActivityProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var viewModelFactory: ProfileViewModelFactory
    private lateinit var adapter: UserAttemptsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_profile)

        initViewModel()
        initRecycler()
        observers()
        expandableLayout()
    }

    private fun initViewModel(){
        val userId: String? =intent.getStringExtra("userId")
        viewModelFactory = ProfileViewModelFactory(userId.toString())
        viewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]
        binding.profileViewModel = viewModel
        binding.lifecycleOwner=this
    }

    private fun initRecycler(){
        binding.userAttemptRecycler.layoutManager=LinearLayoutManager(this)
        adapter= UserAttemptsAdapter(this)
        binding.userAttemptRecycler.adapter=adapter
    }

    private fun observers(){

        viewModel.getUserAttemptList().observe(this){
            if (it!=null){
                adapter.setAttemptList(it)
                adapter.notifyDataSetChanged()
            }
            else{
                Log.i("profileActivity","Error")
            }
        }
    }

    private fun expandableLayout() {
        binding.userAttemptCard.setOnClickListener {

            if (binding.userAttemptRecycler.visibility == View.VISIBLE) {
                // The transition of the hiddenView is carried out by the TransitionManager class.
                // Here we use an object of the AutoTransition Class to create a default transition
                TransitionManager.beginDelayedTransition(binding.userAttemptCard, AutoTransition())
                binding.userAttemptRecycler.visibility = View.GONE
                binding.arrow1.setImageResource(R.drawable.ic_arrow_close)
            } else {
                TransitionManager.beginDelayedTransition(binding.userAttemptCard, AutoTransition())
                binding.userAttemptRecycler.visibility = View.VISIBLE
                binding.arrow1.setImageResource(R.drawable.ic_arrow_down)
            }
        }

    }
}