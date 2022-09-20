package com.example.quizzy.utilities

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class UserDetailsSharedPrefrence {

    private val STATE1 = "email"
    private val STATE2 = "userId"
    private val STATE3 = "name"
    private val emailId=""
    private val userId=""
    private val name=""

    fun getSharedPreferences(ctx: Context?): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    fun setEmailID(ctx: Context?, emailId:String) {
        val editor: SharedPreferences.Editor = getSharedPreferences(ctx).edit()
        editor.putString(STATE1,emailId)
        editor.apply()
    }

    fun getEmailID(ctx: Context?): String?{
        return getSharedPreferences(ctx).getString(STATE1,emailId)
    }

    fun clearDetails(ctx: Context?) {
        val editor: SharedPreferences.Editor = getSharedPreferences(ctx).edit()
        editor.clear()
        editor.apply()
    }

    fun setUserID(ctx: Context?, userID:String) {
        val editor: SharedPreferences.Editor = getSharedPreferences(ctx).edit()
        editor.putString(STATE2,userId)
        editor.apply()
    }

    fun getUserID(ctx: Context?): String?{
        return getSharedPreferences(ctx).getString(STATE2,userId)
    }

    fun setName(ctx: Context?, name:String) {
        val editor: SharedPreferences.Editor = getSharedPreferences(ctx).edit()
        editor.putString(STATE3,name)
        editor.apply()
    }

    fun getName(ctx: Context?): String?{
        return getSharedPreferences(ctx).getString(STATE3,name)
    }
}