package com.app.codeby.priyabrat.utils

import android.content.Context
import android.content.SharedPreferences
import com.app.codeby.priyabrat.Constants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefUtils @Inject constructor(applicationContext: Context) {

    private var pref: SharedPreferences = applicationContext.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)

    fun saveString(key: String, value: String) {
        pref.edit().putString(key, value).apply()
    }

    fun getString(key: String): String? {
        return pref.getString(key, null)
    }

    fun saveLong(key: String, value: Long) {
        pref.edit().putLong(key, value).apply()
    }

    fun getLong(key: String): Long {
        return pref.getLong(key, 0L)
    }
}