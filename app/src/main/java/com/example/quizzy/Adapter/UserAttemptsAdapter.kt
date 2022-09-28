package com.example.quizzy.Adapter

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzy.R
import com.example.quizzy.Screens.ProfileActivity
import com.example.quizzy.dataModel.model.AttemptModelUser
import kotlinx.android.synthetic.main.list_item_user_attempts.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class UserAttemptsAdapter(private val activity: ProfileActivity): RecyclerView.Adapter<UserAttemptsAdapter.MyViewHolder>() {

    private var list: List<AttemptModelUser>? = null

    fun setAttemptList(list: List<AttemptModelUser>?) {
        this.list=list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_user_attempts, parent, false)
        return MyViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list?.get(position)!!,activity)
    }

    override fun getItemCount(): Int {
        return if (list == null) 0
        else list?.size!!
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){

        private val name=view.quizname
        private val score=view.score
        private val time=view.time
        private val date=view.date

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(data: AttemptModelUser, activity: ProfileActivity) {

            resetViews()
            val myDate = Date(data.time)
            val formatter = SimpleDateFormat("HH:mm:ss")
            val myTime = formatter.format(myDate)

            val x = data.startTime
            val localDate: LocalDate = x.toInstant()?.atZone(ZoneId.systemDefault())!!.toLocalDate()
            val year: Int = localDate.year
            val month: Int = localDate.monthValue
            val day: Int = localDate.dayOfMonth

            name.text=data.title
            score.text=data.score.toString()
            date.text="${day}/${month}/${year}"
            time.text=myTime

            if (data.score<data.passingScore){
                score.setTextColor(Color.parseColor("#FF453A"))
            }
        }

        fun resetViews(){
            score.setTextColor(Color.parseColor("#30D158"))
        }

    }

}