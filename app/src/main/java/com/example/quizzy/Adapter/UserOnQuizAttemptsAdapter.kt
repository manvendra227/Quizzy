package com.example.quizzy.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzy.R
import com.example.quizzy.dataModel.model.AttemptModelQuiz
import com.example.quizzy.dataModel.model.AttemptModelQuizUser

class UserOnQuizAttemptsAdapter: RecyclerView.Adapter<UserOnQuizAttemptsAdapter.MyViewHolder>() {


    private var list: List<AttemptModelQuizUser>? = null

    fun setAttemptList(list: List<AttemptModelQuizUser>?) {
        this.list=list
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_user_on_quiz_attempts, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list?.get(position)!!)
    }

    override fun getItemCount(): Int {
        if (list == null) return 0
        else return list?.size!!
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){

        fun bind(data: AttemptModelQuizUser) {

        }

    }
}