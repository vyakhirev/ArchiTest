package com.mikhail.vyakhirev.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mikhail.vyakhirev.data.model.UserModel

@Dao
interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: UserModel)

    @Query("SELECT * FROM users")
    suspend fun loadAllUsers(): List<UserModel>

    @Query("SELECT * FROM users WHERE id= :id")
    suspend fun loadUserByUd(id: String): UserModel

}