package com.mikhail.vyakhirev

import android.content.Context
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Cookie
import javax.inject.Inject

class SharedPrefsUtil @Inject constructor(@ApplicationContext context: Context) {

    private val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    fun saveAuthResult(authCredential: AuthCredential) {
        prefs.edit().putString("auth_result", authCredential.toString()).apply()
    }

//    fun loadAuthResult(): AuthResult {
//        val type = object : TypeToken<AuthResult>() {}.type
//        val string = prefs.getString("auth_result", "") ?: ""
//
//        return try {
//            Gson().fromJson(string, type)
//        } catch (ex: Exception) {
////            AuthResult()
//        }
//    }

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

    fun saveLastQuery(query: String) {
        prefs.edit().putString(LAST_QUERY, query).apply()
    }

    fun loadLastQuery(): String {
        return prefs.getString(LAST_QUERY, "") ?: ""
    }

    companion object {
        const val LAST_QUERY = "last_query"
    }
}
