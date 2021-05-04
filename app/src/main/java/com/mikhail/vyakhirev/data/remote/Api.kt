package com.mikhail.vyakhirev.data.remote

import com.google.firebase.inject.Deferred
import com.mikhail.vyakhirev.data.model.ResponsePhotoItemHolder
import io.reactivex.Flowable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("/services/rest/?method=flickr.photos.getRecent")
    suspend fun getRecent(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("nojsoncallback") noJsonCallback: String = "nojsoncallback",
        @Query("format") format: String = "json"
    ): ResponsePhotoItemHolder

    @GET("/services/rest/?method=flickr.photos.search")
suspend fun getPhotoSearch(
        @Query("text") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("nojsoncallback") noJsonCallback: String = "nojsoncallback",
        @Query("format") format: String = "json"
    ): ResponsePhotoItemHolder

//    flickr.photos.getInfo

    @GET("/services/rest/?method=flickr.photos.getInfo")
    suspend fun getInfo(
        @Query("photo_id") photo_id: String,
        @Query("nojsoncallback") noJsonCallback: String = "nojsoncallback",
        @Query("format") format: String = "json"
    ): ResponsePhotoItemHolder
}