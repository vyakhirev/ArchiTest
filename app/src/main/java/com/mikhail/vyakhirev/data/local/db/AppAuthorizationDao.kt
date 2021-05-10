package com.mikhail.vyakhirev.data.local.db

import androidx.room.*
import com.mikhail.vyakhirev.data.model.AppAuthorizationModel

@Dao
interface AppAuthorizationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun registerUser(user: AppAuthorizationModel)

    @Query("SELECT * FROM authorization_table WHERE login = :login AND password = :password")
    suspend fun checkUser(login: String, password: String):List<AppAuthorizationModel>

    @Update()
    suspend fun setLogged(user: AppAuthorizationModel)

    @Update()
    suspend fun setLogout(user: AppAuthorizationModel)

    @Query("SELECT * FROM authorization_table WHERE isLogged =:flag ")
    suspend fun isLogged(flag:Boolean):List<AppAuthorizationModel>

}