package com.mikhail.vyakhirev.data

import com.mikhail.vyakhirev.data.remote.RetrofitClient
import com.mikhail.vyakhirev.SharedPrefsUtil
import com.mikhail.vyakhirev.data.local.AppDatabase

class Repository (
    private val retrofit: RetrofitClient,
    private val prefs: SharedPrefsUtil,
    private val db: AppDatabase
//        private val signalRClient: SignalRClient
    ) {

}