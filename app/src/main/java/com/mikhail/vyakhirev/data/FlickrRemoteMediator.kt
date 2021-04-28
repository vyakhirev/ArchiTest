package com.mikhail.vyakhirev.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.mikhail.vyakhirev.SharedPrefsUtil
import com.mikhail.vyakhirev.data.local.db.AppDatabase
import com.mikhail.vyakhirev.data.local.db.RemoteKeys
import com.mikhail.vyakhirev.data.model.PhotoItem
import com.mikhail.vyakhirev.data.model.QueryStatModel
import com.mikhail.vyakhirev.data.remote.RetrofitClient
import com.mikhail.vyakhirev.utils.STARTING_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class FlickrRemoteMediator @Inject constructor(
    query: String? = null,
    private val retrofit: RetrofitClient,
    private val db: AppDatabase,
    private val prefsUtil: SharedPrefsUtil
) : RemoteMediator<Int, PhotoItem>() {

    val querySearch = query
    override suspend fun initialize(): InitializeAction {
        // Launch remote refresh as soon as paging starts and do not trigger remote prepend or
        // append until refresh has succeeded. In cases where we don't mind showing out-of-date,
        // cached offline data, we can return SKIP_INITIAL_REFRESH instead to prevent paging
        // triggering remote refresh.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PhotoItem>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for prepend.
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

//        val apiQuery = query + IN_QUALIFIER

        try {
            var apiResponse = if (querySearch.isNullOrBlank()) {
                retrofit.api.getRecent(page, state.config.pageSize)
            } else {
                retrofit.api.getPhotoSearch(querySearch, page, state.config.pageSize)
            }

            val total = apiResponse.photos.total

            val photos = apiResponse.photos.photo
            val endOfPaginationReached = photos.isEmpty()

            val lastQuery = prefsUtil.loadLastQuery()
            db.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    db.remoteKeysDao().clearRemoteKeys()
//                    if (querySearch == lastQuery)
//                        db.photoItemDao().clearButSaveFavor(false)
//                    else
                    db.photoItemDao().clearDb()
                }
                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = photos.map {
                    RemoteKeys(photoId = it.id, prevKey = prevKey, nextKey = nextKey)
                }

                val favorites = db.favoritesDao().loadAllFavorites()
                    .map {
                        it.id
                    }

                photos.map {
                    if (favorites.contains(it.id))
                        it.isFavorite = true
                }


                db.remoteKeysDao().insertAll(keys)
                db.photoItemDao().insertAll(photos)
                db.queryStatDao().insertAll(QueryStatModel(querySearch.toString(), total))
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, PhotoItem>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { it ->
                // Get the remote keys of the last item retrieved
                db.remoteKeysDao().remoteKeysId(it.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, PhotoItem>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { it ->
                // Get the remote keys of the first items retrieved
                db.remoteKeysDao().remoteKeysId(it.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, PhotoItem>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { it ->
                db.remoteKeysDao().remoteKeysId(it)
            }
        }
    }

}