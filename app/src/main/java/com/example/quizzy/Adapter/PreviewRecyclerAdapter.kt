package com.example.quizzy.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzy.R
import com.example.quizzy.Screens.AddQuizFragment
import com.example.quizzy.dataModel.extras.questionFormat
import kotlinx.android.synthetic.main.list_item_question.view.*

class PreviewRecyclerAdapter(val context: AddQuizFragment) :
    RecyclerView.Adapter<PreviewRecyclerAdapter.MyViewHolder>() {

    private var questionList: ArrayList<questionFormat>? = null

    fun setPreviewList(questionList: ArrayList<questionFormat>?) {
        this.questionList = questionList
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PreviewRecyclerAdapter.MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_question, parent, false)
        return PreviewRecyclerAdapter.MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: PreviewRecyclerAdapter.MyViewHolder, position: Int) {
        holder.bind(questionList?.get(position)!!, context)
    }

    override fun getItemCount(): Int {
        if (questionList == null) return 0
        else return questionList?.size!!
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val delete: ImageView = view.delete
        val sNo: TextView = view.questionNum
        val question: TextView = view.question

        val optionA: TextView = view.textA
        val optionB: TextView = view.textB
        val optionC: TextView = view.textC
        val optionD: TextView = view.textD

        val statusA: TextView = view.statusA
        val statusB: TextView = view.statusB
        val statusC: TextView = view.statusC
        val statusD: TextView = view.statusD

        val backA: RelativeLayout = view.backA
        val backB: RelativeLayout = view.backB
        val backC: RelativeLayout = view.backC
        val backD: RelativeLayout = view.backD

        fun bind(data: questionFormat, context: AddQuizFragment) {

            question.text=data.question

            optionA.text=data.options?.get(0)
            optionB.text=data.options?.get(1)
            optionC.text=data.options?.get(2)
            optionD.text=data.options?.get(3)

            when(data.answer){
                0->{
                }
                1->{}
                2->{}
                3->{}
            }
        }
    }

}