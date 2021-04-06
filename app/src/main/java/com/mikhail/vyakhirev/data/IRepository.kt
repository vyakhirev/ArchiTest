package com.mikhail.vyakhirev.data

import com.google.firebase.inject.Deferred
import com.mikhail.vyakhirev.data.model.ResponsePhotoItemHolder
import retrofit2.Response

interface IRepository {
    suspend fun getFavorites(): ResponsePhotoItemHolder
}