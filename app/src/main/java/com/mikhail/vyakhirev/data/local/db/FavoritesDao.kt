package com.mikhail.vyakhirev.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mikhail.vyakhirev.data.model.FavoriteModel

@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(favorite: FavoriteModel)

    @Query("SELECT * FROM favorites")
    suspend fun loadAllFavorites(): List<FavoriteModel>

    @Query("DELETE FROM favorites WHERE id = :id" )
    suspend fun deleteFromFavorites(id:String)
}