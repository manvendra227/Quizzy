package com.example.quizzy.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzy.R
import com.example.quizzy.dataModel.model.AttemptModelQuiz
import com.example.quizzy.dataModel.model.AttemptModelUser

class UserAttemptsAdapter: RecyclerView.Adapter<UserAttemptsAdapter.MyViewHolder>() {

    private var list: List<AttemptModelUser>? = null

    fun setAttemptList(list: List<AttemptModelUser>?) {
        this.list=list
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_user_attempts, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list?.get(position)!!)
    }

    override fun getItemCount(): Int {
        return if (list == null) 0
        else list?.size!!
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){

        fun bind(data: AttemptModelUser) {

        }

    }

}