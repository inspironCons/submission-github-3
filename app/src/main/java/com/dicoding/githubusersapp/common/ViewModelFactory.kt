package com.dicoding.githubusersapp.common

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.githubusersapp.ui.detailuser.DetailViewModel
import com.dicoding.githubusersapp.ui.favorites.FavoritesViewModel
import com.dicoding.githubusersapp.ui.home.HomeViewModel

class ViewModelFactory private constructor(
    private val app:Application,
    private val pref:SettingsPreferences?
):ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(app) as T
            }
            modelClass.isAssignableFrom(FavoritesViewModel::class.java) -> {
                FavoritesViewModel(app) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(pref!!) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application,prefs:SettingsPreferences?=null): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application,prefs)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}