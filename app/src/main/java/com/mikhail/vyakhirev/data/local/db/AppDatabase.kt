package com.mikhail.vyakhirev.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mikhail.vyakhirev.data.model.FavoriteModel
import com.mikhail.vyakhirev.data.model.PhotoItem
import com.mikhail.vyakhirev.data.model.QueryStatModel
import com.mikhail.vyakhirev.data.model.UserModel


@Database(
    entities = [
        PhotoItem::class,
        RemoteKeys::class,
        QueryStatModel::class,
        FavoriteModel::class,
        UserModel::class],
    version = 7,
    exportSchema = false
)
@TypeConverters(AppTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun photoItemDao(): PhotoItemDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun queryStatDao(): QueryStatDao
    abstract fun favoritesDao(): FavoritesDao
    abstract fun usersDao(): UsersDao
}