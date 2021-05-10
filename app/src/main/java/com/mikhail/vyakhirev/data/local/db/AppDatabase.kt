package com.mikhail.vyakhirev.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mikhail.vyakhirev.data.model.*


@Database(
    entities = [
        PhotoItem::class,
        RemoteKeys::class,
        QueryStatModel::class,
        FavoriteModel::class,
        AppAuthorizationModel::class,
        UserModel::class],
    version = 8,
    exportSchema = false
)
@TypeConverters(AppTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun photoItemDao(): PhotoItemDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun queryStatDao(): QueryStatDao
    abstract fun favoritesDao(): FavoritesDao
    abstract fun usersDao(): UsersDao
    abstract fun appAuthorizationDao(): AppAuthorizationDao
}