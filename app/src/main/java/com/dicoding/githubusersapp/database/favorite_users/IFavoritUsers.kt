package com.dicoding.githubusersapp.database.favorite_users

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface IFavoritUsers {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(payload:FavoriteUsersEntity)

    @Query("select * from favorite_users")
    fun getAllFavorite():List<FavoriteUsersEntity>

    @Query("select * from favorite_users where id=:id")
    fun getFavorite(id:Int):LiveData<FavoriteUsersEntity>

}