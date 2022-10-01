package com.example.quizzy.Adapter

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzy.R
import com.example.quizzy.Screens.ProfileActivity
import com.example.quizzy.Screens.QuizDetailActivity
import com.example.quizzy.dataModel.model.AttemptModelUser
import com.example.quizzy.utilities.MyToast
import kotlinx.android.synthetic.main.list_item_user_attempts.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class UserAttemptsAdapter(private val activity: ProfileActivity) :
    RecyclerView.Adapter<UserAttemptsAdapter.MyViewHolder>() {

    private var list: List<AttemptModelUser>? = null

    fun setAttemptList(list: List<AttemptModelUser>?) {
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_user_attempts, parent, false)
        return MyViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list?.get(position)!!, activity)
    }

    override fun getItemCount(): Int {
        return if (list == null) 0
        else list?.size!!
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val name = view.quizname
        private val score = view.score
        private val time = view.time
        private val date = view.date
        private val view = view.view


        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(data: AttemptModelUser, activity: ProfileActivity) {

            resetViews()
            val ms=data.time%1000
            val s=((data.time/1000)%60)-2
            val m=((data.time/1000)/60)%60
            val milliseconds=if (ms<10) "00$ms" else if (ms in 10..99) "0$ms" else "$ms"
            val seconds=if (s<10) "0$s" else "$s"
            val minutes=if (m<10) "0$m" else "$m"

            val x = data.startTime
            val localDate: LocalDate = x.toInstant()?.atZone(ZoneId.systemDefault())!!.toLocalDate()
            val year: Int = localDate.year
            val month: Int = localDate.monthValue
            val day: Int = localDate.dayOfMonth

            name.text = data.title
            score.text = data.score.toString()
            date.text = "${day}/${month}/${year}"
            time.text = "$minutes:$seconds:$milliseconds"

            if (data.score < data.passingScore) {
                score.setTextColor(Color.parseColor("#FF453A"))
            }

            view.setOnClickListener {
                if (!data.quizID.isNullOrEmpty()) {
                    val intent = Intent(activity, QuizDetailActivity::class.java)
                    intent.putExtra("quizId", data.quizID)
                    activity.startActivity(intent)
                }else{
                    Toast.makeText(activity,"No Quiz Found",Toast.LENGTH_SHORT).show()
                }
            }
        }

        fun resetViews() {
            score.setTextColor(Color.parseColor("#30D158"))
        }

    }

}