package com.example.quizzy.utilities

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.quizzy.dataModel.model.SavedUserModel
import com.google.gson.Gson




class UserDetailsSharedPrefrence {

    private val STATE = "user"
    var savedUser=""

    fun getSharedPreferences(ctx: Context?): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    fun setUserDetails(ctx: Context?, emailId:String,userId:String,name:String) {
        val editor: SharedPreferences.Editor = getSharedPreferences(ctx).edit()

        val tempUser=SavedUserModel(userId,name,emailId)
        val gson = Gson()
        val json = gson.toJson(tempUser)
        editor.putString(STATE, json)
        editor.apply()
    }

    fun getUserDetails(ctx: Context?): String?{
        return getSharedPreferences(ctx).getString(STATE,savedUser)
    }

    fun clearUserDetails(ctx: Context?) {
        val editor: SharedPreferences.Editor = getSharedPreferences(ctx).edit()
        editor.clear()
        editor.apply()
    }
}