package com.example.quizzy.utilities

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentActivity

class MyToast(val context: FragmentActivity?) {

    fun showLong(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    fun showShort(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}