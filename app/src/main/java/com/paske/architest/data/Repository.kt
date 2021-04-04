package com.paske.architest.data

import com.paske.architest.data.remote.RetrofitClient
import com.paske.architest.SharedPrefsUtil
import com.paske.architest.data.local.AppDatabase

class Repository (
    private val retrofit: RetrofitClient,
    private val prefs: SharedPrefsUtil,
    private val db: AppDatabase
//        private val signalRClient: SignalRClient
    ) {

}