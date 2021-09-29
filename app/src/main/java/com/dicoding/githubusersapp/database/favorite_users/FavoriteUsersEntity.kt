package com.dicoding.githubusersapp.database.favorite_users

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_users")
data class FavoriteUsersEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id:Int = 0,
    @ColumnInfo(name = "username")
    var username:String,
    @ColumnInfo(name = "avatar")
    var avatar:String
)