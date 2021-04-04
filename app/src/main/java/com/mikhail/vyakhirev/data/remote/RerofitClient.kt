package com.mikhail.vyakhirev.data.remote

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mikhail.vyakhirev.SharedPrefsUtil
import com.mikhail.vyakhirev.utils.BASE_URL
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient(prefsUtil: SharedPrefsUtil){

    private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val okHttpClient = OkHttpClient.Builder()
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

    var gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    val api: Api = Retrofit.Builder()
        .baseUrl("https://$BASE_URL/api/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(Api::class.java)

}