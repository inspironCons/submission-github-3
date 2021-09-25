package com.dicoding.githubusersapp.ui.favorites

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.dicoding.githubusersapp.common.DataState
import com.dicoding.githubusersapp.database.favorite_users.FavoriteUsersEntity
import com.dicoding.githubusersapp.model.UserDetail
import com.dicoding.githubusersapp.model.Users
import com.dicoding.githubusersapp.repository.favorite_users.FavoriteUsersRepository
import kotlinx.coroutines.Dispatchers

class FavoritesViewModel(app:Application):ViewModel() {
    private val favoriteUsersRepository: FavoriteUsersRepository = FavoriteUsersRepository(app)

    fun getAllDataFavorites() = liveData(Dispatchers.IO) {
        try{
            emit(DataState.loading(null,"Loading"))
            val getAll = favoriteUsersRepository.getAllFavorite()
            val dataMap = getAll.map { it.toUsers() }
            emit(DataState.success(dataMap))
        }catch (e:Exception){
            emit(DataState.error(null,e.localizedMessage?:"uknown spesific error"))
        }
    }

    private fun FavoriteUsersEntity.toUsers():Users{
        val data = this
        return Users(
            id = data.id,
            username = data.username,
            avatar = data.avatar
            )
    }
}