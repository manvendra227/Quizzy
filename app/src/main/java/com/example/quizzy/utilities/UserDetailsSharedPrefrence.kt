package com.example.quizzy.utilities

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class UserDetailsSharedPrefrence {

    private val STATE = "email"
    private val emailId=""

    fun getSharedPreferences(ctx: Context?): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    fun setEmailID(ctx: Context?, emailId:String) {
        val editor: SharedPreferences.Editor = getSharedPreferences(ctx).edit()
        editor.putString(STATE,emailId)
        editor.apply()
    }

    fun getEmailID(ctx: Context?): String?{
        return getSharedPreferences(ctx).getString(STATE,emailId)
    }

    fun clearEmailID(ctx: Context?) {
        val editor: SharedPreferences.Editor = getSharedPreferences(ctx).edit()
        editor.clear()
        editor.apply()
    }
}