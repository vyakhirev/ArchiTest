package com.mikhail.vyakhirev

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Cookie
import java.lang.Exception

class SharedPrefsUtil(context: Context) {

    private val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    fun saveCookies(cookie: List<Cookie>) {
        prefs.edit().putString("cookies", Gson().toJson(cookie)).apply()
    }

    fun loadCookies(): List<Cookie> {
        val type = object : TypeToken<List<Cookie>>() {}.type
        val string = prefs.getString("cookies", "") ?: ""

        return try {
            Gson().fromJson(string, type)
        } catch (ex: Exception) {
            emptyList()
        }
    }


}
