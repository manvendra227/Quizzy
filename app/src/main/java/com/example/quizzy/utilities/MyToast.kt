package com.example.quizzy.utilities

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.quizzy.R
import io.github.muddz.styleabletoast.StyleableToast

class MyToast(val context: FragmentActivity?) {

    fun showLong(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun showShort(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun styleToast(context: Context,message: String){
        StyleableToast.makeText(context,message, R.style.feedbackToast,Toast.LENGTH_LONG).show()
    }
}