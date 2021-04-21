package com.mikhail.vyakhirev.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mikhail.vyakhirev.SharedPrefsUtil
import com.mikhail.vyakhirev.data.local.db.AppDatabase
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
    //    override suspend fun getFavorites(page:Int,perPage:Int): ResponsePhotoItemHolder {
//        return retrofit.api.getRecent(page,perPage)
//    }
    private val pagingSourceFactory = { db.photoItemDao().getTop() }

    @OptIn(ExperimentalPagingApi::class)
    override fun getFavorites(): Flow<PagingData<PhotoItem>> =
        Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = FlickrRemoteMediator(
                null,
                retrofit.api,
                db
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow

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
                "Moon",
                retrofit.api,
                db
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow

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