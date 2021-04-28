package com.mikhail.vyakhirev.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mikhail.vyakhirev.data.model.FavoriteModel
import com.mikhail.vyakhirev.data.model.PhotoItem
import com.mikhail.vyakhirev.data.model.QueryStatModel


@Database(entities = [PhotoItem::class, RemoteKeys::class, QueryStatModel::class,FavoriteModel::class], version = 6, exportSchema = false)
//@androidx.room.TypeConverters(TypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun photoItemDao(): PhotoItemDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun queryStatDao(): QueryStatDao
    abstract fun favoritesDao(): FavoritesDao
}