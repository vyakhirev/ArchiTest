package com.mikhail.vyakhirev.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.mikhail.vyakhirev.data.local.db.AppTypeConverters

@Entity(tableName = "users")
data class UserModel(
    @PrimaryKey var id: String,
    var name: String?,
    var imageUrl: String?,
    var email: String?,
    @TypeConverters(AppTypeConverters::class)
    var queryList: List<String>
)