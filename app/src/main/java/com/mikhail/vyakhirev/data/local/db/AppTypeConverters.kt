package com.mikhail.vyakhirev.data.local.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import java.util.stream.Collectors

class AppTypeConverters {

    @TypeConverter
    fun fromString(value: String?): List<String> {
        val listType = object :
            TypeToken<ArrayList<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }

//    @TypeConverter
//    fun toPreviousStaffList(previousStaff: String?): List<Basket.PreviousStaff>? {
//        return if (previousStaff != null) {
//            val type = object : TypeToken<List<Basket.PreviousStaff>>() {}.type
//            Gson().fromJson(previousStaff, type)
//        } else {
//            emptyList()
//        }
}
