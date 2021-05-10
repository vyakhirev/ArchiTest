package com.mikhail.vyakhirev.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.auth.AuthCredential
import com.mikhail.vyakhirev.SharedPrefsUtil
import com.mikhail.vyakhirev.data.local.db.AppDatabase
import com.mikhail.vyakhirev.data.model.AppAuthorizationModel
import com.mikhail.vyakhirev.data.model.FavoriteModel
import com.mikhail.vyakhirev.data.model.PhotoItem
import com.mikhail.vyakhirev.data.remote.RetrofitClient
import com.mikhail.vyakhirev.utils.NETWORK_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val retrofit: RetrofitClient,
    private val prefs: SharedPrefsUtil,
    private val db: AppDatabase
//        private val signalRClient: SignalRClient
) : IRepository {

    private val pagingSourceFactory = { db.photoItemDao().getTop() }

    override suspend fun getFavorites(): List<FavoriteModel> {
        return db.favoritesDao().loadAllFavorites()
    }

    @ExperimentalPagingApi
    override fun getPhotoSearchResult(
        query: String
    ): Flow<PagingData<PhotoItem>> =
        Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = FlickrRemoteMediator(
                query = query,
                retrofit,
                db,
                prefs
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow

    override fun saveAuthResult(authCredential: AuthCredential) {
        prefs.saveAuthResult(authCredential)
    }


    override fun saveQueryToPrefs(query: String) = prefs.saveLastQuery(query)

    override fun loadQueryFromPrefs(): String = prefs.loadLastQuery()

    override suspend fun getStatByQuery(): Int {
        return db.queryStatDao().getStatByQuery(loadQueryFromPrefs())?.total ?: 0
    }

    override suspend fun switchFavorite(photoItem: PhotoItem) {
//        db.photoItemDao().switchFavorites(photoItem)
        if (photoItem.isFavorite)
            db.favoritesDao().addToFavorites(
                FavoriteModel(
                    id = photoItem.id,
                    title = photoItem.title,
                    imageUrl = photoItem.getFlickrImageLink(),
                    isFavorite = true
                )
            )
        else
            db.favoritesDao().deleteFromFavorites(photoItem.id)
    }

    override suspend fun deleteFavorite(photoItemId: String) {
        db.favoritesDao().deleteFromFavorites(photoItemId)
    }

    override suspend fun getPhotoItemByID(id: String): PhotoItem {
        return retrofit.api.getInfo(id).photos.photo.first()
    }

    override suspend fun registerUser(login: String, email: String, password: String) {
        db.appAuthorizationDao().registerUser(
            AppAuthorizationModel(
                login = login,
                email = email,
                password = password
            )
        )
    }

    override suspend fun isAccessGranted(user: String, password: String): Boolean {
        val users = db.appAuthorizationDao().checkUser(user, password)
        return if (users.isNotEmpty()) {
            users.first().isLogged = true
            db.appAuthorizationDao().setLogged(users.first())
            true
        } else
            false
    }

    override suspend fun isUserLoggedNow(): Boolean {
        return db.appAuthorizationDao().isLogged(true).isNotEmpty()
    }

    override suspend fun loadUserFromAppDb(): AppAuthorizationModel {
        return db.appAuthorizationDao().isLogged(true).first()
    }

    override suspend fun logoutUser(user: AppAuthorizationModel) {
        db.appAuthorizationDao().setLogout(user)
    }

}


//    fun getSearchResultStream(query: String): Flow<PagingData<PhotoItem>> {
//        return Pager(
//            config = PagingConfig(
//                pageSize = NETWORK_PAGE_SIZE,
//                enablePlaceholders = false
//            ),
//            pagingSourceFactory = { FlickrPagingSource() }
//        ).flow
//    }
//    companion object{
//       private const val NETWORK_PAGE_SIZE=30
//    }
//}