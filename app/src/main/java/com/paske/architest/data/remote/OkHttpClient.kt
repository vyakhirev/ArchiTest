package com.paske.architest.data.remote

import com.paske.architest.SharedPrefsUtil
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class OkHttpClient(prefsUtil: SharedPrefsUtil) {

    val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    val okHttpClient = OkHttpClient.Builder()
        .cookieJar(object : CookieJar {
            override fun saveFromResponse(
                url: HttpUrl,
                cookies: List<Cookie>
            ) {
                prefsUtil.saveCookies(cookies)
            }

            override fun loadForRequest(url: HttpUrl): List<Cookie> {
                return prefsUtil.loadCookies()
            }
        })
        .addInterceptor(logger)
        .build()
}
