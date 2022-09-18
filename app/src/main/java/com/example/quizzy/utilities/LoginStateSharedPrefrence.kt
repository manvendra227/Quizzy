package com.example.quizzy.utilities

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager


class LoginStateSharedPrefrence {

    private val STATE = "loginState"
    private val isLogined=false

    fun getSharedPreferences(ctx: Context?): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    fun setState(ctx: Context?,loginState:Boolean) {
        val editor: SharedPreferences.Editor = getSharedPreferences(ctx).edit()
        editor.putBoolean(STATE,loginState)
        editor.apply()
    }

    fun getState(ctx: Context?): Boolean {
        return getSharedPreferences(ctx).getBoolean(STATE,isLogined)
    }

    fun clearState(ctx: Context?) {
        val editor: SharedPreferences.Editor = getSharedPreferences(ctx).edit()
        editor.clear()
        editor.apply()
    }
}