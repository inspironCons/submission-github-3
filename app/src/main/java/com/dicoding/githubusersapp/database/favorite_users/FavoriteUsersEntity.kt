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
    @ColumnInfo(name = "name")
    var name:String,
    @ColumnInfo(name = "avatar")
    var avatar:String,
    @ColumnInfo(name = "company")
    var company:String,
    @ColumnInfo(name = "location")
    var location:String,
    @ColumnInfo(name = "repository")
    var repository:Int,
    @ColumnInfo(name = "follower")
    var follower:Int,
    @ColumnInfo(name = "following")
    var following:Int
)