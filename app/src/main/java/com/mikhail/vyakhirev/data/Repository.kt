package com.mikhail.vyakhirev.data

import com.google.firebase.inject.Deferred
import com.mikhail.vyakhirev.SharedPrefsUtil
import com.mikhail.vyakhirev.data.local.AppDatabase
import com.mikhail.vyakhirev.data.model.ResponsePhotoItemHolder
import com.mikhail.vyakhirev.data.remote.RetrofitClient
import retrofit2.Response

class Repository(
    private val retrofit: RetrofitClient,
    private val prefs: SharedPrefsUtil,
    private val db: AppDatabase
//        private val signalRClient: SignalRClient
) : IRepository {
    override suspend fun getFavorites(): ResponsePhotoItemHolder {
        return retrofit.api.getRecent(1, 10)
    }

}