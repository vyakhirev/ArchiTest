package com.mikhail.vyakhirev

import android.app.Application
import androidx.room.Room
import com.google.firebase.FirebaseApp
import com.mikhail.vyakhirev.data.Repository
import com.mikhail.vyakhirev.data.local.AppDatabase
import com.mikhail.vyakhirev.data.remote.RetrofitClient
import com.mikhail.vyakhirev.presentation.detail_fragment.DetailViewModelFactory
import com.mikhail.vyakhirev.presentation.list_fragment.ListViewModelFactory
import com.mikhail.vyakhirev.presentation.main_activity.MainActivityViewModelFactory
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
        bind() from singleton { RetrofitClient(instance()) }
        bind() from singleton { String() }

        bind() from singleton {
            Room.databaseBuilder(this@App, AppDatabase::class.java, "app_db")
                .fallbackToDestructiveMigration()
                .build()
        }

        bind() from singleton { Repository(instance(), instance(), instance()) }

        bind() from singleton { MainActivityViewModelFactory(instance()) }
        bind() from singleton { ListViewModelFactory(instance()) }
        bind() from singleton { DetailViewModelFactory(instance()) }

    }

}