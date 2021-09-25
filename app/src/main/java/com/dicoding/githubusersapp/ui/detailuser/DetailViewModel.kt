package com.dicoding.githubusersapp.ui.detailuser

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.githubusersapp.model.UserDetail
import com.dicoding.githubusersapp.common.ServiceBuilder
import com.dicoding.githubusersapp.network.users.IUsers
import com.dicoding.githubusersapp.repository.favorite_users.FavoriteUsersRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DetailViewModel(app:Application): ViewModel() {
    private val detailUser = MutableLiveData<UserDetail>()
    private val catchErrorMessage = MutableLiveData<String>()
    val getCatchErrorMessage:LiveData<String> get() = catchErrorMessage
    private val request = ServiceBuilder.buildService(IUsers::class.java)
    private val favoriteUsersRepository:FavoriteUsersRepository = FavoriteUsersRepository(app)

    fun getDetailUser(username:String){
        try{
            viewModelScope.launch {
                val user = request.getDetailUsername(username)
                val userDetail = UserDetail()
                userDetail.username = user.login
                userDetail.name = user.name
                userDetail.avatar = user.avatarUrl
                userDetail.repository = user.publicRepos
                userDetail.location = user.location
                userDetail.company = user.company
                userDetail.follower = user.followers
                userDetail.following = user.following
                detailUser.postValue(userDetail)
            }

        }catch (e:HttpException){
            catchErrorMessage.postValue("${e.message()} - ${e.code()}")
        }catch (e:Exception){
            catchErrorMessage.postValue(e.localizedMessage)
        }
    }

    fun getDataDetail():LiveData<UserDetail>{
        return detailUser
    }
}