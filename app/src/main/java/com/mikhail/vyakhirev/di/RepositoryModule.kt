package com.mikhail.vyakhirev.di

import com.mikhail.vyakhirev.SharedPrefsUtil
import com.mikhail.vyakhirev.data.IRepository
import com.mikhail.vyakhirev.data.Repository
import com.mikhail.vyakhirev.data.local.db.AppDatabase
import com.mikhail.vyakhirev.data.remote.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(
        retrofit: RetrofitClient,
        prefs: SharedPrefsUtil,
        db: AppDatabase
    ): IRepository {
        return Repository(
            retrofit, prefs, db
        )
    }
}