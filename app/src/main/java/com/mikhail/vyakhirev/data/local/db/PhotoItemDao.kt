package com.mikhail.vyakhirev.data.local.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mikhail.vyakhirev.data.model.PhotoItem

@Dao
interface PhotoItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<PhotoItem>)

    @Query("SELECT * FROM photos")
    fun getTop():  PagingSource<Int, PhotoItem>

    @Query("DELETE FROM photos")
    suspend fun clearDb()
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertSingleItem(photoItem: PhotoItem)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertMultipleItem(photoItemList: List<PhotoItem>): Completable
//
//    @Query("SELECT * FROM photos WHERE id = :itemId")
//    fun getItemByItemId(itemId: Int): Flowable<PhotoItem>
//
//    @Query("SELECT * FROM photos WHERE isFavorite = :itemIsFavorite")
//    fun getFavorites(itemIsFavorite:Boolean): Flowable<List<PhotoItem>>
//
//    @Query("SELECT * FROM photos ")
//    fun searchPhotosByTitle(): Flowable<List<PhotoItem>>
//
//    @Query("SELECT * FROM photos")
//    fun fetchItems(): Flowable<List<PhotoItem>>
//
//    @Update
//    fun updateItem(photoItem: PhotoItem): Completable
//
//    @Delete
//    fun deleteItem(photoItem: PhotoItem)
}