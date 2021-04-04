package com.paske.architest

import android.app.Application
import androidx.room.Room
import com.google.firebase.FirebaseApp
import com.paske.architest.data.Repository
import com.paske.architest.data.local.AppDatabase
import com.paske.architest.data.remote.OkHttpClient
import com.paske.architest.data.remote.RetrofitClient
import com.paske.architest.presentation.detail_fragment.DetailViewModelFactory
import com.paske.architest.presentation.list_fragment.ListViewModelFactory
import com.paske.architest.presentation.main_activity.MainActivityViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class App : Application(), KodeinAware {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(baseContext)
    }

    override val kodein = Kodein.lazy {

        bind() from singleton { SharedPrefsUtil(this@App) }
        bind() from singleton { OkHttpClient(instance()) }
        bind() from singleton { RetrofitClient(instance()) }

        bind() from singleton {
            Room.databaseBuilder(this@App, AppDatabase::class.java, "paske_db")
                .fallbackToDestructiveMigration()
                .build()
        }

        bind() from singleton { Repository(instance(), instance(), instance()) }

        bind() from singleton { MainActivityViewModelFactory(instance()) }
        bind() from singleton { ListViewModelFactory(instance()) }
        bind() from singleton { DetailViewModelFactory(instance()) }

    }

}