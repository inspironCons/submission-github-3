package com.dicoding.githubusersapp.database.favorite_users

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface IFavoritUsers {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(payload:FavoriteUsersEntity)

    @Query("select * from favorite_users")
    fun getAllFavorite():LiveData<List<FavoriteUsersEntity>>

    @Query("select count(*) from favorite_users where id=:id")
    fun getFavorite(id:Int):Int

    @Delete
    fun delete(user:FavoriteUsersEntity)

}