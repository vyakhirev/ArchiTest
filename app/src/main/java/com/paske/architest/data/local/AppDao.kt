package com.paske.architest.data.local

import androidx.room.*
import com.paske.architest.data.model.PhotoItem
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface PhotoItemDao {
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