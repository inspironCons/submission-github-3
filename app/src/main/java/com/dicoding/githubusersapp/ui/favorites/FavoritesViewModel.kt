package com.dicoding.githubusersapp.ui.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dicoding.githubusersapp.database.favorite_users.FavoriteUsersEntity
import com.dicoding.githubusersapp.repository.favorite_users.FavoriteUsersRepository

class FavoritesViewModel(app:Application):AndroidViewModel(app) {
    private val favoriteUsersRepository: FavoriteUsersRepository = FavoriteUsersRepository(app)

    fun getAllDataFavorites():LiveData<List<FavoriteUsersEntity>> = favoriteUsersRepository.getAllFavorite()

}