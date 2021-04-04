package com.paske.architest.data.remote

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.paske.architest.utils.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient(client: OkHttpClient) {

    var gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    val api: Api = Retrofit.Builder()
        .baseUrl("https://$BASE_URL/api/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(Api::class.java)

}