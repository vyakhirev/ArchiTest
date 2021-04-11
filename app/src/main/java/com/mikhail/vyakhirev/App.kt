package com.mikhail.vyakhirev

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

//    override fun onCreate() {
//        super.onCreate()
//        FirebaseApp.initializeApp(baseContext)
//    }

//    override val di = DI.lazy {
//
//        bind { singleton { SharedPrefsUtil(this@App) } }
//        bind { singleton { RetrofitClient(instance()) } }
//        bind { singleton { String() } }
//
//        bind {
//            singleton {
//                Room.databaseBuilder(this@App, AppDatabase::class.java, "app_db")
//                    .fallbackToDestructiveMigration()
//                    .build()
//            }
//        }
//
//        bind<IRepository>() with singleton { Repository(instance(), instance(), instance()) }
//
//        bind { singleton { MainActivityViewModelFactory(instance()) } }
//        bind { singleton { ListFragmentViewModelFactory(instance()) } }
//        bind { singleton { DetailViewModelFactory(instance()) } }
//        bind { singleton { FavoritesViewModelFactory(instance()) } }
//        bind { singleton { SettingsViewModelFactory(instance()) } }
//
//    }

}