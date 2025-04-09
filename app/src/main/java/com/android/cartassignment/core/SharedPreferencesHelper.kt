package com.android.cartassignment.core

import android.content.Context
import android.content.SharedPreferences
import com.android.cartassignment.core.Constants.EMPTY_STRING

object SharedPreferencesHelper {
    private const val PREFS_NAME = "ShoppingAppPrefs"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveString(context: Context, key: String, value: String) {
        val editor = getPreferences(context).edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(context: Context, key: String): String? {
        return getPreferences(context).getString(key, EMPTY_STRING)
    }
}