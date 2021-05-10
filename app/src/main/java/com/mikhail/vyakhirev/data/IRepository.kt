package com.mikhail.vyakhirev.data

import androidx.paging.PagingData
import com.google.firebase.auth.AuthCredential
import com.mikhail.vyakhirev.data.model.AppAuthorizationModel
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

    suspend fun deleteFavorite(photoItemId: String)

    suspend fun getPhotoItemByID(id: String): PhotoItem

    suspend fun registerUser(login: String, email: String, password: String)

    suspend fun isAccessGranted(user: String, password: String): Boolean

    suspend fun isUserLoggedNow(): Boolean

    suspend fun loadUserFromAppDb(): AppAuthorizationModel

    suspend fun logoutUser(user: AppAuthorizationModel)
}