package com.example.quizzy.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzy.R
import com.example.quizzy.Screens.ReviewActivity
import com.example.quizzy.dataModel.extras.questionFormat
import kotlinx.android.synthetic.main.list_item_question.view.*
import org.w3c.dom.Text

class ReviewQuizAdapter(val activity: ReviewActivity) :
    RecyclerView.Adapter<ReviewQuizAdapter.MyViewHolder>() {

    var questionList: List<questionFormat>? = null
    var userAnswer: List<Int>? = null

    fun setReviewList(questionList: List<questionFormat>?) {
        this.questionList = questionList
    }

    fun setUserAnswerList(userAnswer: List<Int>?) {
        this.userAnswer = userAnswer
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_question, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(questionList?.get(position)!!, userAnswer?.get(position)!!, position)
    }

    override fun getItemCount(): Int {
        if (questionList == null) return 0
        else return questionList?.size!!
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val delete: ImageView = view.delete
        private val sNo: TextView = view.questionNum
        private val question: TextView = view.question

        private val optionA: TextView = view.textA
        private val optionB: TextView = view.textB
        private val optionC: TextView = view.textC
        private val optionD: TextView = view.textD

        private val statusA: TextView = view.statusA
        private val statusB: TextView = view.statusB
        private val statusC: TextView = view.statusC
        private val statusD: TextView = view.statusD

        private val scoreA: TextView = view.scoreA
        private val scoreB: TextView = view.scoreB
        private val scoreC: TextView = view.scoreC
        private val scoreD: TextView = view.scoreD

        private val backA = view.backA
        private val backB = view.backB
        private val backC = view.backC
        private val backD = view.backD

        fun bind(data: questionFormat, userAnswer: Int, position: Int) {

            delete.visibility = View.GONE
            question.text = data.question

            optionA.text = data.options?.get(0)
            optionB.text = data.options?.get(1)
            optionC.text = data.options?.get(2)
            optionD.text = data.options?.get(3)

            sNo.text = "${(position + 1)} . "

            when (data.answer) {
                "0" -> {
                    setAnsView(backA)
                }
                "1" -> {
                    setAnsView(backB)
                }
                "2" -> {
                    setAnsView(backC)
                }
                "3" -> {
                    setAnsView(backD)
                }
            }

            if (userAnswer == data.answer.toInt()) {
                when(userAnswer){
                    0->setPositiveAnsView(statusA,scoreA)
                    1->setPositiveAnsView(statusB,scoreB)
                    2->setPositiveAnsView(statusC,scoreC)
                    3->setPositiveAnsView(statusD,scoreD)
                }
            } else {
                when (userAnswer) {
                    0->{ setNegativeAnsView(backA,statusA,scoreA) }
                    1->{ setNegativeAnsView(backB,statusB,scoreB) }
                    2->{ setNegativeAnsView(backC,statusC,scoreC) }
                    3->{ setNegativeAnsView(backD,statusD,scoreD) }
                }
            }
        }

        private fun setAnsView(view: RelativeLayout) {
            view.setBackgroundColor(Color.parseColor("#30D158"))
        }

        private fun setNegativeAnsView(view: RelativeLayout, status: TextView,score:TextView) {
            view.setBackgroundColor(Color.parseColor("#FF453A"))
            status.visibility = View.VISIBLE
            status.text="INCORRECT!"
            score.visibility=View.VISIBLE
            score.text="-2"
        }
        private fun setPositiveAnsView(status: TextView, score: TextView){
            status.visibility=View.VISIBLE
            score.visibility=View.VISIBLE
        }
    }

}