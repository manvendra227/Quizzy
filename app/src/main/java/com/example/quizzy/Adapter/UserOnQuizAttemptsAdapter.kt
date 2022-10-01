package com.example.quizzy.Adapter

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzy.R
import com.example.quizzy.dataModel.model.AttemptModelQuizUser
import kotlinx.android.synthetic.main.list_item_user_on_quiz_attempts.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class UserOnQuizAttemptsAdapter(private val passingScore:Double): RecyclerView.Adapter<UserOnQuizAttemptsAdapter.MyViewHolder>() {


    private var list: List<AttemptModelQuizUser>? = null

    fun setAttemptList(list: List<AttemptModelQuizUser>?) {
        this.list=list
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_user_on_quiz_attempts, parent, false)
        return MyViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list?.get(position)!!,passingScore)
    }

    override fun getItemCount(): Int {
        if (list == null) return 0
        else return list?.size!!
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){

        private val score=view.score_1
        private val time=view.time_1
        private val date=view.date_1

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(data: AttemptModelQuizUser, passingScore: Double) {

            val ms=data.time%1000
            val s=((data.time/1000)%60)-2
            val m=((data.time/1000)/60)%60
            val milliseconds=if (ms<10) "00$ms" else if (ms in 10..99) "0$ms" else "$ms"
            val seconds=if (s<10) "0$s" else "$s"
            val minutes=if (m<10) "0$m" else "$m"

            val x = data.timestamp
            val localDate: LocalDate = x?.toInstant()?.atZone(ZoneId.systemDefault())!!.toLocalDate()
            val year: Int = localDate.year
            val month: Int = localDate.monthValue
            val day: Int = localDate.dayOfMonth

            score.text=data.score.toString()
            date.text= "${day}/${month}/${year}"
            time.text = "$minutes:$seconds:$milliseconds"
            if (data.score<passingScore){
                score.setTextColor(Color.parseColor("#FF453A"))
            }
        }

    }
}