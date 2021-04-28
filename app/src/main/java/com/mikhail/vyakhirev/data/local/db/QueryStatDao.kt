package com.mikhail.vyakhirev.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mikhail.vyakhirev.data.model.QueryStatModel

@Dao
interface QueryStatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(queryStat: QueryStatModel)

    @Query("SELECT * FROM query_stat WHERE `query` = :query")
    suspend fun getStatByQuery(query: String): QueryStatModel?

    @Query("DELETE FROM query_stat")
    suspend fun clearRemoteKeys()
}