package com.dicoding.githubusersapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.githubusersapp.common.Constant
import com.dicoding.githubusersapp.database.favorite_users.FavoriteUsersEntity
import com.dicoding.githubusersapp.database.favorite_users.IFavoritUsers

@Database(entities = [
    FavoriteUsersEntity::class
],version = 1)
abstract class GithubDatabase :RoomDatabase(){
    abstract fun favoritUsersDao():IFavoritUsers

    companion object{
        @Volatile
        private var INSTANCE: GithubDatabase? = null

        @JvmStatic
        fun getInstace(context: Context):GithubDatabase{
            if (INSTANCE == null){
                synchronized(GithubDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context, GithubDatabase::class.java, Constant.DB_NAME
                    )
                        .build()
                }
            }

            return INSTANCE as GithubDatabase

        }
    }

}