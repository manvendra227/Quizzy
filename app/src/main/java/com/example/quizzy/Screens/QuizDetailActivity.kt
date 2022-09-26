package com.example.quizzy.Screens

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.quizzy.Adapter.QuizTagAdapter
import com.example.quizzy.R
import com.example.quizzy.databinding.ActivityQuizDetailBinding
import com.example.quizzy.databinding.PopupTimerWarningBinding
import com.example.quizzy.utilities.MyToast
import com.example.quizzy.viewModels.QuizDetailViewModel
import com.example.quizzy.viewModels.ViewModelFactory.QuizDetailsViewModelFactory
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.android.synthetic.main.activity_quiz_detail.*
import java.util.*
import kotlin.concurrent.schedule

@RequiresApi(Build.VERSION_CODES.O)
class QuizDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizDetailBinding
    private val toast = MyToast(this)
    private lateinit var viewModel: QuizDetailViewModel
    private lateinit var viewModelFactory: QuizDetailsViewModelFactory
    private lateinit var quizTagAdapter: QuizTagAdapter
    private lateinit var quizId: String

    private lateinit var dialogBinding: PopupTimerWarningBinding
    private lateinit var dialogWarning: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz_detail)

        quizId = intent.getStringExtra("quizId").toString()
        initViewModel(quizId)
        viewModel.getData()

        initRecyclerView()
        loadTags()
        clickEvents()

    }

    private fun initViewModel(quizId: String) {

        viewModelFactory = QuizDetailsViewModelFactory(quizId)
        viewModel = ViewModelProvider(this, viewModelFactory)[QuizDetailViewModel::class.java]
        binding.quizDetailViewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun initRecyclerView() {

        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW

        tag_recycler.layoutManager = layoutManager
        quizTagAdapter = QuizTagAdapter(this)
        tag_recycler.adapter = quizTagAdapter

    }

    private fun clickEvents() {
        binding.buttonStart.setOnClickListener {
            dialogWarning()
        }
    }

    private fun dialogWarning() {
        if (viewModel.isTimed.value == true) {

            dialogBinding = PopupTimerWarningBinding.inflate(layoutInflater)
            dialogWarning = Dialog(this)
            dialogWarning.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogWarning.setContentView(dialogBinding.root)

            dialogBinding.startButton.setOnClickListener {
                startQuiz()
                dialogWarning.dismiss()
            }
            dialogWarning.show()
        } else {
            startQuiz()
        }
    }

    private fun startQuiz() {
        layoutDetails.visibility= View.GONE
        countdown.visibility=View.VISIBLE
        val handler = Handler()
        handler.postDelayed({
            startActivity(Intent(this, QuizActivity::class.java).putExtra("quizId", quizId).putExtra("time", viewModel.time.value))
            finish()
        }, 2200)

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadTags() {

        viewModel.getTagsList().observe(this, Observer {
            if (it != null) {
                quizTagAdapter.setTagList(it)
                quizTagAdapter.notifyDataSetChanged()
            } else {
                toast.showLong("No tags found error")
            }
        })
    }
}