package com.example.quizzy.Screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.quizzy.R
import com.example.quizzy.databinding.FragmentAddQuizBinding

class AddQuizFragment : Fragment() {

    private lateinit var binding: FragmentAddQuizBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_quiz, container, false)

        return binding.root
    }

}