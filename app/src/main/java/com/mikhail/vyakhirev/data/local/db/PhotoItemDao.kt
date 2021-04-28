package com.mikhail.vyakhirev.data.local.db

import androidx.paging.PagingSource
import androidx.room.*
import com.mikhail.vyakhirev.data.model.PhotoItem

@Dao
interface PhotoItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(repos: List<PhotoItem>)

    @Query("SELECT * FROM photos")
    fun getTop():  PagingSource<Int, PhotoItem>

    @Query("DELETE FROM photos WHERE isFavorite = :fal")
    suspend fun clearButSaveFavor(fal:Boolean)

    @Query("DELETE FROM photos")
    suspend fun clearDb()

    @Query("SELECT * FROM photos WHERE isFavorite = :setFavorite")
    suspend fun loadFavorites(setFavorite:Boolean):List<PhotoItem>

    @Update
    suspend fun switchFavorites(photoItem: PhotoItem)

//    @Query("SELECT * FROM photos")
//    suspend fun loadAllFavorites(): PagingSource<Int, PhotoItem>
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
    @Delete
    fun deleteItem(photoItem: PhotoItem)
}