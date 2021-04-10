package com.mikhail.vyakhirev.data.remote

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mikhail.vyakhirev.SharedPrefsUtil
import com.mikhail.vyakhirev.utils.BASE_URL
import com.mikhail.vyakhirev.utils.FLICKR_API_KEY
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient(prefsUtil: SharedPrefsUtil) {

    private val loggerInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val authInterceptor = Interceptor { chain ->
        val newUrl = chain.request().url
            .newBuilder()
            .addQueryParameter("api_key", FLICKR_API_KEY)
            .build()

        val newRequest = chain.request()
            .newBuilder()
            .url(newUrl)
            .build()

        chain.proceed(newRequest)
    }

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
        .addInterceptor(authInterceptor)
        .addInterceptor(loggerInterceptor)
        .build()

    var gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    val api: Api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(Api::class.java)

}