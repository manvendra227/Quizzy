package com.example.quizzy.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzy.R
import com.example.quizzy.Screens.QuizDetailActivity
import kotlinx.android.synthetic.main.list_item_tag.view.*

class QuizTagAdapter(private val activity: QuizDetailActivity) : RecyclerView.Adapter<QuizTagAdapter.MyViewHolder>() {

    private var tags: List<String>? = null

    fun setTagList(tags: List<String>?) {
        this.tags = tags
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_tag, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(tags?.get(position)!!,activity)
    }

    override fun getItemCount(): Int {
        if (tags == null) return 0
        else return tags?.size!!
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tagText=view.tag_text

        fun bind(data: String, activity: QuizDetailActivity) {
            tagText.text=data

        }
    }
}