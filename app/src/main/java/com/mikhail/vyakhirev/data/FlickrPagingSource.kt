package com.mikhail.vyakhirev.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mikhail.vyakhirev.data.model.PhotoItem
import com.mikhail.vyakhirev.data.remote.Api
import com.mikhail.vyakhirev.utils.NETWORK_PAGE_SIZE
import com.mikhail.vyakhirev.utils.STARTING_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException


class FlickrPagingSource(
   private val service:Api,
//    private val query: String,
) : PagingSource<Int, PhotoItem>() {

    override fun getRefreshKey(state: PagingState<Int, PhotoItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoItem> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = service.getRecent(position,params.loadSize)
            val repos = response.photos.photo
            val nextKey = if (repos.isEmpty()) {
                null
            } else {
                // initial load size = 3 * NETWORK_PAGE_SIZE
                // ensure we're not requesting duplicating items, at the 2nd request
                position + (params.loadSize / NETWORK_PAGE_SIZE)
            }
            LoadResult.Page(
                data = repos,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}