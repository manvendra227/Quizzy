package com.example.quizzy.Screens

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizzy.Adapter.QuizAttemptsAdapter
import com.example.quizzy.Adapter.QuizTagAdapter
import com.example.quizzy.Adapter.UserOnQuizAttemptsAdapter
import com.example.quizzy.R
import com.example.quizzy.dataModel.model.SavedUserModel
import com.example.quizzy.databinding.ActivityQuizDetailBinding
import com.example.quizzy.databinding.PopupTimerWarningBinding
import com.example.quizzy.utilities.MyToast
import com.example.quizzy.utilities.UserDetailsSharedPrefrence
import com.example.quizzy.viewModels.QuizDetailViewModel
import com.example.quizzy.viewModels.ViewModelFactory.QuizDetailsViewModelFactory
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_quiz_detail.*

@RequiresApi(Build.VERSION_CODES.O)
class QuizDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizDetailBinding
    private val toast = MyToast(this)
    private lateinit var viewModel: QuizDetailViewModel
    private lateinit var viewModelFactory: QuizDetailsViewModelFactory
    private lateinit var quizTagAdapter: QuizTagAdapter
    private lateinit var quizAttemptsAdapter: QuizAttemptsAdapter
    private lateinit var userAttemptsAdapter: UserOnQuizAttemptsAdapter
    private lateinit var quizId: String
    private var userDetails = UserDetailsSharedPrefrence()

    private lateinit var dialogBinding: PopupTimerWarningBinding
    private lateinit var dialogWarning: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz_detail)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        quizId = intent.getStringExtra("quizId").toString()
        initViewModel(quizId)

        initRecyclerViewTags()
        initRecyclerQuizAttempts()
        initRecyclerUserAttempts()
        loadTags()
        loadQuizAttempts()
        loadUserAttempts()
        clickEvents()
        expandableLayout()

    }

    private fun initViewModel(quizId: String) {

        val gson = Gson()
        val savedUserResponse = userDetails.getUserDetails(application)
        val savedUserModel = gson.fromJson(savedUserResponse, SavedUserModel::class.java)

        viewModelFactory = QuizDetailsViewModelFactory(quizId,savedUserModel.userId)
        viewModel = ViewModelProvider(this, viewModelFactory)[QuizDetailViewModel::class.java]
        binding.quizDetailViewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun initRecyclerViewTags() {

        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW

        tag_recycler.layoutManager = layoutManager
        quizTagAdapter = QuizTagAdapter(this)
        tag_recycler.adapter = quizTagAdapter

    }

    private fun initRecyclerQuizAttempts(){

        binding.quizAttemptRecycler.layoutManager=LinearLayoutManager(this)
        quizAttemptsAdapter= QuizAttemptsAdapter(viewModel.passing.value?.toDouble() ?: 30.0,this)
        binding.quizAttemptRecycler.adapter=quizAttemptsAdapter
    }

    private fun initRecyclerUserAttempts(){

        binding.userAttemptRecycler.layoutManager=LinearLayoutManager(this)
        userAttemptsAdapter= UserOnQuizAttemptsAdapter(viewModel.passing.value?.toDouble() ?: 30.0)
        binding.userAttemptRecycler.adapter=userAttemptsAdapter

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

    private fun loadQuizAttempts(){

        viewModel.getQuizAttemptList().observe(this){
            if (it!=null){
                quizAttemptsAdapter.setAttemptList(it)
                quizAttemptsAdapter.notifyDataSetChanged()
            }else {
                toast.showLong("Error in quizAttempts")
            }
        }
    }

    private fun loadUserAttempts(){

        viewModel.getUserAttemptList().observe(this){
            if (it!=null){
                userAttemptsAdapter.setAttemptList(it)
                userAttemptsAdapter.notifyDataSetChanged()
            }else {
                toast.showLong("Error in userAttempts")
            }
        }
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
            dialogWarning.window?.attributes!!.windowAnimations = R.style.DialogAnimation

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
        layoutDetails.visibility = View.GONE
        appbar.visibility=View.GONE
        countdown.visibility = View.VISIBLE
        val handler = Handler()
        handler.postDelayed({
            startActivity(
                Intent(this, QuizActivity::class.java).putExtra("quizId", quizId)
                    .putExtra("time", viewModel.time.value)
            )
            finish()
        }, 2200)

    }

    private fun expandableLayout() {
        binding.quizAttemptCard.setOnClickListener {

            if (binding.quizAttemptRecycler.visibility == View.VISIBLE) {
                // The transition of the hiddenView is carried out by the TransitionManager class.
                // Here we use an object of the AutoTransition Class to create a default transition
                TransitionManager.beginDelayedTransition(binding.quizAttemptCard, AutoTransition())
                binding.quizAttemptRecycler.visibility = View.GONE
                binding.arrow1.setImageResource(R.drawable.ic_arrow_close)
            } else {
                TransitionManager.beginDelayedTransition(binding.quizAttemptCard, AutoTransition())
                binding.quizAttemptRecycler.visibility = View.VISIBLE
                binding.arrow1.setImageResource(R.drawable.ic_arrow_down)
            }
        }
        binding.userAttemptCard.setOnClickListener {

            if (binding.userAttemptRecycler.visibility == View.VISIBLE) {
                // The transition of the hiddenView is carried out by the TransitionManager class.
                // Here we use an object of the AutoTransition Class to create a default transition
                TransitionManager.beginDelayedTransition(binding.userAttemptCard, AutoTransition())
                binding.userAttemptRecycler.visibility = View.GONE
                binding.arrow2.setImageResource(R.drawable.ic_arrow_close)
            } else {
                TransitionManager.beginDelayedTransition(binding.userAttemptCard, AutoTransition())
                binding.userAttemptRecycler.visibility = View.VISIBLE
                binding.arrow2.setImageResource(R.drawable.ic_arrow_down)
            }
        }
    }
}