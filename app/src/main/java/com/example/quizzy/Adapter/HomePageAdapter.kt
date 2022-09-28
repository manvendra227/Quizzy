package com.example.quizzy.Adapter

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzy.R
import com.example.quizzy.Screens.ListActivity
import com.example.quizzy.Screens.ProfileActivity
import com.example.quizzy.Screens.QuizDetailActivity
import com.example.quizzy.dataModel.enums.Difficulty
import com.example.quizzy.dataModel.model.QuizShortModel
import com.example.quizzy.dataModel.model.SavedUserModel
import com.example.quizzy.utilities.UserDetailsSharedPrefrence
import com.google.gson.Gson
import kotlinx.android.synthetic.main.list_item_quiz.view.*
import kotlinx.android.synthetic.main.popup_warning.view.*
import java.text.DecimalFormat

class HomePageAdapter(val activity: Activity, val key: String) :
    RecyclerView.Adapter<HomePageAdapter.MyViewHolder>() {

    private var quizList: List<QuizShortModel>? = null

    fun setQuizList(quizList: List<QuizShortModel>?) {
        this.quizList = quizList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_quiz, parent, false)
        return MyViewHolder(view, activity)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(quizList?.get(position)!!, activity, key)
    }

    override fun getItemCount(): Int {
        if (quizList == null) return 0
        else return quizList?.size!!
    }


    class MyViewHolder(view: View, val activity: Activity) : RecyclerView.ViewHolder(view) {

        private val df: DecimalFormat = DecimalFormat("0.00")
        private val authorName = view.username
        val title = view.quiz_title
        val desc = view.quiz_desc
        val rating = view.rating
        val timesPlayed = view.no_of_rating
        val difficulty = view.difficulty
        val button = view.attempt_button
        val profile = view.profile
        val delete = view.delete


        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(data: QuizShortModel, activity: Activity, key: String) {
            authorName.text = data.authorName
            title.text = data.title
            desc.text = data.description
            rating.text = df.format(data.avgRating)
            timesPlayed.text = data.timesPlayed.toString()
            delete.visibility = View.GONE

            when (data.difficulty) {
                Difficulty.EASY -> difficulty.setImageResource(R.drawable.diff_easy)
                Difficulty.MEDIUM -> difficulty.setImageResource(R.drawable.diff_med)
                Difficulty.HARD -> difficulty.setImageResource(R.drawable.diff_hard)
            }

            button.setOnClickListener {
                val intent = Intent(activity, QuizDetailActivity::class.java)
                intent.putExtra("quizId", data.quizId)
                activity.startActivity(intent)
            }

            profile.setOnClickListener {
                val intent = Intent(activity, ProfileActivity::class.java)
                intent.putExtra("userId", data.authorID)
                activity.startActivity(intent)
            }

            if (activity is ListActivity) {

                /**userdetails*/
                val json = UserDetailsSharedPrefrence().getUserDetails(activity)
                val savedUser = Gson().fromJson(json, SavedUserModel::class.java)

                if (key == savedUser.userId) {
                    delete.visibility = View.VISIBLE

                    delete.setOnClickListener {
                        dialogWarningDelete()
                    }
                }
            }

        }

        private fun dialogWarningDelete() {
            val box = LayoutInflater.from(activity).inflate(R.layout.popup_warning, null)
            val dialog =
                AlertDialog.Builder(activity, R.style.Theme_Transparent).setView(box).create()
            dialog.window?.attributes!!.windowAnimations = R.style.DialogAnimation
            dialog.show()

            box.lottie.setAnimation("delete.json")
            box.text1.text =
                "Deleting quiz will delete all it's related data from server. You still want to delete?"

        }
    }

}