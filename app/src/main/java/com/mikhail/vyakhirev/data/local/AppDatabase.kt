package com.mikhail.vyakhirev.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mikhail.vyakhirev.data.model.PhotoItem


@Database(entities = [PhotoItem::class], version = 1, exportSchema = false)
//@androidx.room.TypeConverters(TypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun photoItemDao(): PhotoItemDao
}