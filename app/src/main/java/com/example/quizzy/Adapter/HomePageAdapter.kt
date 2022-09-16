package com.example.quizzy.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzy.R
import com.example.quizzy.Screens.HomeActivity
import com.example.quizzy.dataModel.model.QuizShortModel
import kotlinx.android.synthetic.main.layout_create_quiz.view.*
import kotlinx.android.synthetic.main.list_item_quiz.view.*
import kotlinx.android.synthetic.main.popup_rating.view.*

class HomePageAdapter(val activity: HomeActivity) : RecyclerView.Adapter<HomePageAdapter.MyViewHolder>() {

    private var quizList: List<QuizShortModel>? = null

    fun setQuizList(quizList: List<QuizShortModel>?) {
        this.quizList = quizList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_quiz, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(quizList?.get(position)!!,activity)
    }

    override fun getItemCount(): Int {
        if (quizList == null) return 0
        else return quizList?.size!!
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val authorName = view.username
        val title = view.quiz_title
        val desc = view.quiz_desc
        val rating = view.rating
        val timesPlayed = view.no_of_rating
//        val difficulty = view.difficulty

        fun bind(data: QuizShortModel,activity: HomeActivity) {
            authorName.text = data.authorName
            title.text = data.title
            desc.text = data.description
            rating.text = data.avgRating.toString()
            timesPlayed.text = data.timesPlayed.toString()
        }
    }

}