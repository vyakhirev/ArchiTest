package com.mikhail.vyakhirev.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "authorization_table")
data class AppAuthorizationModel (
    @PrimaryKey
    var login:String,
    var password:String,
    var email:String,
    var isLogged:Boolean = false
){
//    @PrimaryKey(autoGenerate = true)
//    var id:Int = 0
}