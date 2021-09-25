package com.dicoding.githubusersapp.repository.favorite_users

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.githubusersapp.database.GithubDatabase
import com.dicoding.githubusersapp.database.favorite_users.FavoriteUsersEntity
import com.dicoding.githubusersapp.database.favorite_users.IFavoritUsers
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUsersRepository(app:Application) {
    private val favoritUserDao:IFavoritUsers
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = GithubDatabase.getInstace(app)
        favoritUserDao = db.favoritUsersDao()
    }

    fun insertData(payload:FavoriteUsersEntity){
        executorService.execute { favoritUserDao.insert(payload) }
    }
    fun getAllFavorite():List<FavoriteUsersEntity> = favoritUserDao.getAllFavorite()
    fun getFavorite(id:Int):LiveData<FavoriteUsersEntity> = favoritUserDao.getFavorite(id)
}