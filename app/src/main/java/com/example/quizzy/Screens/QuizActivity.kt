package com.example.quizzy.Screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.quizzy.Adapter.ProgressAdapter
import com.example.quizzy.R
import com.example.quizzy.dataModel.enums.Progress
import com.example.quizzy.dataModel.model.ProgressModel
import com.example.quizzy.databinding.ActivityQuizBinding
import com.example.quizzy.utilities.GridColumnCalculator
import com.example.quizzy.utilities.MyToast
import com.example.quizzy.viewModels.QuizViewModel
import kotlinx.android.synthetic.main.activity_quiz.*


class QuizActivity : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false
    private val toast = MyToast(this)
    private lateinit var binding: ActivityQuizBinding
    private lateinit var viewModel: QuizViewModel
    private lateinit var progressAdapater: ProgressAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        initViewModel()
        initRecycler()
        loadPage()

    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        toast.showShort("Please click again to exit")
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            doubleBackToExitPressedOnce = false
        }, 2000)
    }

    private fun initRecycler() {
        val mNoOfColumns: Int = GridColumnCalculator.calculateNoOfColumns(applicationContext, 46.0f)
        progress_recycler.layoutManager = GridLayoutManager(this, mNoOfColumns)
        progressAdapater = ProgressAdapter(this)
        progress_recycler.adapter = progressAdapater
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[QuizViewModel::class.java]
        binding.quizViewModel = viewModel
        binding.lifecycleOwner = this
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadPage() {

//        viewModel.getProgressList().observe(this, Observer {
//            if (it != null) {
//                progressAdapater.setProgressList(it)
//                progressAdapater.notifyDataSetChanged()
//            } else {
//                toast.showLong("Error in progress List")
//            }
//        })

        val list: List<ProgressModel> = List(25) { ProgressModel(Progress.UNMARKED) }
        progressAdapater.setProgressList(list)
        progressAdapater.notifyDataSetChanged()
    }
}