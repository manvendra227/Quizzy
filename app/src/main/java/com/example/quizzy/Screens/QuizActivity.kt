package com.example.quizzy.Screens

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.quizzy.Adapter.ProgressAdapter
import com.example.quizzy.R
import com.example.quizzy.dataModel.enums.BuzzType
import com.example.quizzy.databinding.ActivityQuizBinding
import com.example.quizzy.utilities.GridColumnCalculator
import com.example.quizzy.utilities.MyToast
import com.example.quizzy.viewModels.QuizViewModel
import com.example.quizzy.viewModels.ViewModelFactory.QuizViewModelFactory
import io.github.muddz.styleabletoast.StyleableToast
import kotlinx.android.synthetic.main.activity_quiz.*
import kotlinx.android.synthetic.main.popup_quiz_result.view.*


class QuizActivity : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false
    private val toast = MyToast(this)
    private lateinit var binding: ActivityQuizBinding
    private lateinit var viewModel: QuizViewModel
    private lateinit var viewModelFactory: QuizViewModelFactory
    private lateinit var progressAdapter: ProgressAdapter
    private lateinit var quizId: String
    private lateinit var time: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        quizId = intent.getStringExtra("quizId").toString()
        time = intent.getStringExtra("time").toString()
        Log.i("mess", "$quizId  and  $time")


        initViewModel(quizId)
        initRecycler()
        loadPage()
        clickEvents()
        observers()

    }

    private fun initRecycler() {
        val mNoOfColumns: Int = GridColumnCalculator.calculateNoOfColumns(applicationContext, 48.0f)
        progress_recycler.layoutManager = GridLayoutManager(this, mNoOfColumns)
        progressAdapter = ProgressAdapter(this) { index -> chooseQuestion(index) }
        progress_recycler.adapter = progressAdapter
    }

    private fun initViewModel(quizId: String) {

        viewModelFactory = QuizViewModelFactory(quizId, time)
        viewModel = ViewModelProvider(this, viewModelFactory)[QuizViewModel::class.java]
        binding.quizViewModel = viewModel
        binding.lifecycleOwner = this
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadPage() {

        viewModel.getProgressList().observe(this, Observer {
            if (it != null) {
                progressAdapter.setProgressList(it)
                progressAdapter.notifyDataSetChanged()
            } else {
                toast.showLong("Error in progress List")
            }
        })

    }

    private fun clickEvents() {
        val handler = Handler()
        viewModel.index.observe(this) {
            if (it >= 0 && it < viewModel.noOfQuestions.value!!) {
                handler.postDelayed({
                    viewModel.setQuestion(index = it)
                    button_prev.visibility = View.VISIBLE
                    button_next.visibility = View.VISIBLE
                }, 250)

            }
            if (it <= 0) {
                handler.postDelayed({ button_prev.visibility = View.GONE }, 250)
            }
            if (it >= viewModel.noOfQuestions.value!! - 1) {
                handler.postDelayed({ button_next.visibility = View.GONE }, 250)

            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun observers() {

        viewModel.currentTime.observe(this) { newTime ->
            setTimer(newTime)
        }

        viewModel.eventQuizFinished.observe(this) { isFinished ->
            if (isFinished) {
                dialogueResult()
            }
        }

        viewModel.eventBuzz.observe(this, Observer { currentBuzz ->
            if (currentBuzz != BuzzType.NO_BUZZ) {
                buzz(currentBuzz.pattern)
                viewModel.onBuzzComplete()
            }
        })

        viewModel.selection.observe(this) {
            resetColors()
            when (it) {
                0 -> {
                    setGreen(optA)
                }
                1 -> {
                    setGreen(optB)
                }
                2 -> {
                    setGreen(optC)
                }
                3 -> {
                    setGreen(optD)
                }
            }
        }
    }

    private fun dialogueFeedback() {
        val feedback = LayoutInflater.from(this).inflate(R.layout.popup_rating, null)
        AlertDialog.Builder(this, R.style.Theme_Transparent).setView(feedback).show()
    }

    private fun dialogueResult() {

        val score= LayoutInflater.from(this).inflate(R.layout.popup_quiz_result, null)
        AlertDialog.Builder(this, R.style.Theme_Transparent).setView(score).show().setCancelable(false)

        score.text2.text = "You scored ${viewModel.percentage.value}%"
        score.text3.text = "${viewModel.counter.value}/${viewModel.noOfQuestions.value}"
        if (viewModel.isPassed.value == true) {
            score.text1.text = "Congratulations! You have passed"
            score.won_emoji.visibility = View.VISIBLE
            score.lost_emoji.visibility = View.INVISIBLE
        } else {
            score.text1.text = "You didn’t get passing score"
            score.won_emoji.visibility = View.INVISIBLE
            score.lost_emoji.visibility = View.VISIBLE
        }

        score.close_button.setOnClickListener {
            val intent=Intent(this,QuizDetailActivity::class.java)
            intent.putExtra("quizId",quizId)
            startActivity(intent)
            finish()
        }

    }

    private fun chooseQuestion(index: Int) {
        viewModel.selectQuestion(index)
        progressAdapter.notifyDataSetChanged()
    }

    /**
     * Given a pattern, this method makes sure the device buzzes
     */
    private fun buzz(pattern: LongArray) {
        val buzzer = getSystemService<Vibrator>()
        buzzer?.let {
            // Vibrate for 500 milliseconds
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                //deprecated in API 26
                buzzer.vibrate(pattern, -1)
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun setTimer(newTime: Long) {
        if (newTime > 0) {
            val seconds = if ((newTime % 60) > 9) "${newTime % 60}" else "0${newTime % 60}"
            val minutes =
                if (((newTime / 60).toInt()) > 9) "${((newTime / 60).toInt()) % 60}" else "0${(newTime / 60).toInt()}"
            val hours = ((newTime / 60).toInt()) / 60
            binding.toolbarTextView.text = " 0${hours}:${minutes}:${seconds}"
        } else {
            binding.toolbarTextView.text = "Time Over"
        }
        if (newTime == 30L) {
            StyleableToast.makeText(this, "30 seconds left", R.style.errorToast).show()
        }
        if (newTime < 30) {
            toolbar.animation = AnimationUtils.loadAnimation(this, R.anim.slow_blinking)
        }
        if (newTime < 10) {
            toolbar.animation = AnimationUtils.loadAnimation(this, R.anim.blinking)
        }
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

    private fun setGreen(view: RelativeLayout) {
        view.setBackgroundColor(Color.parseColor("#30D158"))
    }

    private fun setWhite(view: RelativeLayout) {
        view.setBackgroundColor(Color.parseColor("#FFFFFF"))
    }

    private fun resetColors() {
        setWhite(optA)
        setWhite(optB)
        setWhite(optC)
        setWhite(optD)
    }
}