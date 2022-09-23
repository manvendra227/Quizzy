package com.example.quizzy.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.persistableBundleOf
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzy.R
import com.example.quizzy.Screens.QuizActivity
import com.example.quizzy.dataModel.enums.Progress
import com.example.quizzy.dataModel.model.ProgressModel
import kotlinx.android.synthetic.main.list_item_progress.view.*

class ProgressAdapter(private val activity: QuizActivity ,val onClickProgress: (Int) -> Unit) : RecyclerView.Adapter<ProgressAdapter.MyViewHolder>() {

    private var progress: List<ProgressModel>? = null

    fun setProgressList(progress: List<ProgressModel>?) {
        this.progress=progress
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_progress, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(progress?.get(position)!!,activity)
    }

    override fun getItemCount(): Int {
        if (progress == null) return 0
        else return progress?.size!!
    }


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val progressBox=view.progressBox

        fun bind(data: ProgressModel, activity: QuizActivity) {

            when(data.progress){
                Progress.UNMARKED->{progressBox.setCardBackgroundColor(Color.parseColor("#DCDCDC"))}
                Progress.MARKED->{progressBox.setCardBackgroundColor(Color.parseColor("#48B725"))}
                Progress.PINNED->{progressBox.setCardBackgroundColor(Color.parseColor("#FFB912"))}
                else -> {progressBox.setCardBackgroundColor(Color.parseColor("#000000"))}
            }

            progressBox.setOnClickListener {
                onClickProgress(adapterPosition)
            }
        }
    }
}