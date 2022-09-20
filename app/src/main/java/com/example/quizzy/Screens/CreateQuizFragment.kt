package com.example.quizzy.Screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.quizzy.R
import com.example.quizzy.databinding.ActivityPostQuizBinding
import com.example.quizzy.databinding.FragmentCreateQuizBinding
import com.example.quizzy.viewModels.PostQuizViewModel
import kotlinx.android.synthetic.main.fragment_create_quiz.*


class CreateQuizFragment : Fragment() {

    private lateinit var binding:FragmentCreateQuizBinding
    private lateinit var viewModel:PostQuizViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_create_quiz, container, false)

        clickEvents()
        initViewModel()

        return binding.root
    }

    fun clickEvents(){
        binding.next.setOnClickListener {view:View ->
            view.findNavController().navigate(R.id.action_createQuizFragment_to_addQuizFragment)
        }
    }

    fun initViewModel(){
        viewModel=ViewModelProvider(requireActivity()).get(PostQuizViewModel::class.java)
        binding.createQuizModel=viewModel
        binding.lifecycleOwner=this
    }
}