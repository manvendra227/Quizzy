package com.example.quizzy.Adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzy.R
import com.example.quizzy.dataModel.extras.questionFormat
import kotlinx.android.synthetic.main.list_item_question.view.*


class PreviewRecyclerAdapter(val context: FragmentActivity, val onClickDelete: (Int) -> Unit) :
    RecyclerView.Adapter<PreviewRecyclerAdapter.MyViewHolder>() {

    var questionList: List<questionFormat>? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setPreviewList(questionList: List<questionFormat>?) {
        this.questionList = questionList
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_question, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(questionList?.get(position)!!, position)
    }

    override fun getItemCount(): Int {
        return if (questionList == null) 0
        else questionList?.size!!
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val delete: ImageView = view.delete
        private val sNo: TextView = view.questionNum
        private val question: TextView = view.question

        private val optionA: TextView = view.textA
        private val optionB: TextView = view.textB
        private val optionC: TextView = view.textC
        private val optionD: TextView = view.textD

        private val statusA: TextView = view.scoreA
        private val statusB: TextView = view.scoreB
        private val statusC: TextView = view.scoreC
        private val statusD: TextView = view.scoreD

        private val backA = view.backA
        private val backB = view.backB
        private val backC = view.backC
        private val backD = view.backD


        @SuppressLint("ResourceAsColor", "NotifyDataSetChanged")
        fun bind(data: questionFormat, position: Int) {

            resetViews()
            question.text = data.question

            optionA.text = data.options?.get(0)
            optionB.text = data.options?.get(1)
            optionC.text = data.options?.get(2)
            optionD.text = data.options?.get(3)

            sNo.text = "${(position + 1)} . "

            when (data.answer) {
                "0" -> {
                    backA.setBackgroundColor(Color.parseColor("#30D158"))
                    statusA.visibility = View.VISIBLE
                }
                "1" -> {
                    backB.setBackgroundColor(Color.parseColor("#30D158"))
                    statusB.visibility = View.VISIBLE
                }
                "2" -> {
                    backC.setBackgroundColor(Color.parseColor("#30D158"))
                    statusC.visibility = View.VISIBLE
                }
                "3" -> {
                    backD.setBackgroundColor(Color.parseColor("#30D158"))
                    statusD.visibility = View.VISIBLE

                }
            }

            //passing the index to activity on button click
            delete.setOnClickListener {
                onClickDelete(position)
            }
        }

        private fun resetViews(){
            backA.setBackgroundColor(Color.parseColor("#FFFFFF"))
            backB.setBackgroundColor(Color.parseColor("#FFFFFF"))
            backC.setBackgroundColor(Color.parseColor("#FFFFFF"))
            backD.setBackgroundColor(Color.parseColor("#FFFFFF"))
            statusA.visibility = View.INVISIBLE
            statusB.visibility = View.INVISIBLE
            statusC.visibility = View.INVISIBLE
            statusD.visibility = View.INVISIBLE
        }
    }

}