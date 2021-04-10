package com.mikhail.vyakhirev

import android.app.Application
import androidx.room.Room
import com.google.firebase.FirebaseApp
import com.mikhail.vyakhirev.data.IRepository
import com.mikhail.vyakhirev.data.Repository
import com.mikhail.vyakhirev.data.local.db.AppDatabase
import com.mikhail.vyakhirev.data.remote.RetrofitClient
import com.mikhail.vyakhirev.presentation.detail_fragment.DetailViewModelFactory
import com.mikhail.vyakhirev.presentation.favorites_fragment.FavoritesViewModelFactory
import com.mikhail.vyakhirev.presentation.list_fragment.ListFragmentViewModelFactory
import com.mikhail.vyakhirev.presentation.main_activity.MainActivityViewModelFactory
import com.mikhail.vyakhirev.presentation.settings_fragment.SettingsViewModel
import com.mikhail.vyakhirev.presentation.settings_fragment.SettingsViewModelFactory
import org.kodein.di.*

class App : Application(), DIAware {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(baseContext)
    }

    override val di = DI.lazy {

        bind { singleton { SharedPrefsUtil(this@App) } }
        bind { singleton { RetrofitClient(instance()) } }
        bind { singleton { String() } }

        bind {
            singleton {
                Room.databaseBuilder(this@App, AppDatabase::class.java, "app_db")
                    .fallbackToDestructiveMigration()
                    .build()
            }
        }

        bind<IRepository>() with singleton { Repository(instance(), instance(), instance()) }

        bind { singleton { MainActivityViewModelFactory(instance()) } }
        bind { singleton { ListFragmentViewModelFactory(instance()) } }
        bind { singleton { DetailViewModelFactory(instance()) } }
        bind { singleton { FavoritesViewModelFactory(instance()) } }
        bind { singleton { SettingsViewModelFactory(instance()) } }

    }

}