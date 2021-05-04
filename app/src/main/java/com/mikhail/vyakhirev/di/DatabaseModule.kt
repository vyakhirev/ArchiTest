package com.mikhail.vyakhirev.di

import android.content.Context
import androidx.room.Room
import com.mikhail.vyakhirev.data.local.db.AppDatabase
import com.mikhail.vyakhirev.data.local.db.PhotoItemDao
import com.mikhail.vyakhirev.data.local.db.RemoteKeysDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "app_database.db"
        ).build()
    }

    @Provides
    fun providePhotoItemDao(appDatabase: AppDatabase): PhotoItemDao {
        return appDatabase.photoItemDao()
    }

    @Provides
    fun provideRemoteKeysDao(appDatabase: AppDatabase): RemoteKeysDao {
        return appDatabase.remoteKeysDao()
    }
}