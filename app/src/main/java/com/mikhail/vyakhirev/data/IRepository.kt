package com.mikhail.vyakhirev.data

import androidx.paging.PagingData
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.mikhail.vyakhirev.data.model.FavoriteModel
import com.mikhail.vyakhirev.data.model.PhotoItem
import kotlinx.coroutines.flow.Flow

interface IRepository {
    suspend fun getFavorites(): List<FavoriteModel>

    fun getPhotoSearchResult(
        query: String
    ): Flow<PagingData<PhotoItem>>

    fun saveAuthResult(authCredential: AuthCredential)

    fun saveQueryToPrefs(query: String)

    fun loadQueryFromPrefs(): String

    suspend fun getStatByQuery(): Int

    suspend fun switchFavorite(photoItem: PhotoItem)

    suspend fun switchFavorite(photoItemId: String)

    suspend fun getPhotoItemByID(id: String): PhotoItem
}