package com.example.quizzy.Screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnScrollChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizzy.Adapter.PreviewRecyclerAdapter
import com.example.quizzy.R
import com.example.quizzy.databinding.FragmentAddQuizBinding
import com.example.quizzy.viewModels.PostQuizViewModel
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.android.synthetic.main.popup_rating.view.*


class AddQuizFragment : Fragment() {

    private lateinit var binding: FragmentAddQuizBinding
    private lateinit var viewModel: PostQuizViewModel
    private lateinit var mAdapter:PreviewRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_quiz, container, false)

        initViewModel()
        initRecyclerPreview()
        loadPreview()
        errors()
        previewExpandableLayout()

        return binding.root
    }


    private fun initViewModel() {
        viewModel = ViewModelProvider(requireActivity())[PostQuizViewModel::class.java]
        binding.addQuizViewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun errors(){
        viewModel.errorQues.observe(requireActivity()){binding.questionString.error=viewModel.errorQues.value}
        viewModel.errorA.observe(requireActivity()){binding.optionBoxA.error=viewModel.errorA.value}
        viewModel.errorB.observe(requireActivity()){binding.optionBoxB.error=viewModel.errorB.value}
        viewModel.errorC.observe(requireActivity()){binding.optionBoxC.error=viewModel.errorC.value}
        viewModel.errorD.observe(requireActivity()){binding.optionBoxD.error=viewModel.errorD.value}
        viewModel.errorAns.observe(requireActivity()){
            StyleableToast.makeText(requireActivity(),viewModel.errorAns.value,R.style.errorToast).show()}
    }

    private fun initRecyclerPreview(){
        binding.previewRecycler.layoutManager=LinearLayoutManager(requireActivity())
        mAdapter= PreviewRecyclerAdapter(requireActivity())
        binding.previewRecycler.adapter=mAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadPreview(){
        viewModel.getQuestionList().observe(requireActivity(), Observer {
            if (it!=null){
                mAdapter.setPreviewList(it.reversed())
                mAdapter.notifyDataSetChanged()
            }else{
                Log.i("lul","Error")
            }
        })
    }

    private fun previewExpandableLayout(){
        binding.previewCard.setOnClickListener {

            if (binding.previewRecycler.visibility == View.VISIBLE) {
                // The transition of the hiddenView is carried out by the TransitionManager class.
                // Here we use an object of the AutoTransition Class to create a default transition
                TransitionManager.beginDelayedTransition(binding.previewCard, AutoTransition())
                binding.previewRecycler.visibility = View.GONE
                binding.arrow.setImageResource(R.drawable.ic_arrow_close)
            } else {
                TransitionManager.beginDelayedTransition(binding.previewCard, AutoTransition())
                binding.previewRecycler.visibility = View.VISIBLE
                binding.arrow.setImageResource(R.drawable.ic_arrow_down)
            }
        }
    }

}