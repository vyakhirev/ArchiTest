package com.mikhail.vyakhirev.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mikhail.vyakhirev.data.model.PhotoItem


@Database(entities = [PhotoItem::class, RemoteKeys::class], version = 3, exportSchema = false)
//@androidx.room.TypeConverters(TypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun photoItemDao(): PhotoItemDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}