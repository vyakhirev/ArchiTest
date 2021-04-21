package com.mikhail.vyakhirev.data

import androidx.paging.PagingData
import com.google.firebase.inject.Deferred
import com.mikhail.vyakhirev.data.model.PhotoItem
import com.mikhail.vyakhirev.data.model.ResponsePhotoItemHolder
import io.reactivex.Flowable
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface IRepository {
    fun getFavorites(): Flow<PagingData<PhotoItem>>
    fun getPhotoSearchResult(
        query: String
    ): Flow<PagingData<PhotoItem>>

}