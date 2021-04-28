package com.mikhail.vyakhirev.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "query_stat")
data class QueryStatModel(
    @PrimaryKey val query:String,
    val total:Int
)
