package com.dicoding.githubusersapp.repository.favorite_users

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.githubusersapp.common.DataState
import com.dicoding.githubusersapp.database.GithubDatabase
import com.dicoding.githubusersapp.database.favorite_users.FavoriteUsersEntity
import com.dicoding.githubusersapp.database.favorite_users.IFavoritUsers
import com.dicoding.githubusersapp.model.Users
import kotlinx.coroutines.Dispatchers
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUsersRepository(app:Application) {
    private val favoritUserDao: IFavoritUsers
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = GithubDatabase.getInstace(app)
        favoritUserDao = db.favoritUsersDao()
    }

    fun insertData(payload: Users) {
        executorService.execute { favoritUserDao.insert(payload.toEntity()) }
    }

    fun deleteData(user: Users) {
        executorService.execute {
            favoritUserDao.delete(user.toFavoriteEntity())
        }
    }

    fun getAllFavorite():LiveData<List<FavoriteUsersEntity>> = favoritUserDao.getAllFavorite()

    fun countFavorite(id:Int) = liveData(Dispatchers.IO) {
        try {
            val count = favoritUserDao.getFavorite(id)
            if(count > 0){
                emit(DataState.success(true))
            }else{
                emit(DataState.success(false))
            }
        }catch (e:Exception){
            emit(DataState.error(false,e.localizedMessage?:"an unknown error occurred"))
        }
    }

    private fun Users.toEntity():FavoriteUsersEntity{
        return FavoriteUsersEntity(
            id = this.id,
            username = this.username,
            avatar = this.avatar
        )
    }

    private fun Users.toFavoriteEntity():FavoriteUsersEntity{
        return FavoriteUsersEntity(
            id = this.id,
            username = this.username,
            avatar = this.avatar
        )
    }
}