package com.paske.architest.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoItem(
    @field:PrimaryKey
    var id: String,
    var owner: String,
    var secret: String,
    var server: String,
    var farm: Int,
    var title: String?,
    var ispublic: Short,
    var url_n: String?,
    var width_n: String?,
    var height_n: String?,
    var isFavorite: Boolean = false
) {
    fun getFlickrImageLink(
        size: Char = 'w'
    ): String {
        return "https://farm$farm.staticflickr.com/$server/${id}_${secret}_$size.jpg"
    }
}
