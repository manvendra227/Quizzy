package com.example.quizzy.Screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.quizzy.R
import com.example.quizzy.databinding.FragmentCreateQuizBinding
import com.example.quizzy.viewModels.PostQuizViewModel


class CreateQuizFragment : Fragment() {

    private lateinit var binding: FragmentCreateQuizBinding
    private lateinit var viewModel: PostQuizViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_quiz, container, false)

        initViewModel()
        clickEvents()
        errors()

        return binding.root
    }

    private fun initViewModel() {

        viewModel = ViewModelProvider(requireActivity())[PostQuizViewModel::class.java]
        binding.createQuizModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun clickEvents() {

        viewModel.checkPass.observe(requireActivity(), Observer { value ->
            if (value) {
                viewModel.resetStatus()
                findNavController().navigate(R.id.action_createQuizFragment_to_addQuizFragment)
            }
        })

        viewModel.time.observe(requireActivity()){viewModel.onTimerClick()}
    }

    private fun errors(){
        viewModel.errorTitle.observe(requireActivity()) { binding.title.error = viewModel.errorTitle.value }
        viewModel.errorTags.observe(requireActivity()) { binding.tags.error = viewModel.errorTags.value }
    }


}