package com.example.quizzy.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzy.R
import com.example.quizzy.Screens.HomeActivity
import kotlinx.android.synthetic.main.tag.view.*

class SearchTagAdapter(val activity: HomeActivity) : RecyclerView.Adapter<SearchTagAdapter.MyViewHolder>() {

    private var tags: List<String>? = null

    fun setTagList(tags: List<String>?) {
        this.tags = tags
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tag, parent, false)
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

        private var colors= arrayListOf("#9DFF7A","#97F6E5","#E8FF5E","#61FDB2","#EEEEEE","#4cb0fe","#ffa4ff","#FFF769","#A08AE5")
        val tagText=view.tag_text
        val tagItem=view.tag_item

        fun bind(data: String, activity: HomeActivity) {
            tagText.text=data
            tagItem.setCardBackgroundColor(Color.parseColor(colors.random()))
        }
    }
}