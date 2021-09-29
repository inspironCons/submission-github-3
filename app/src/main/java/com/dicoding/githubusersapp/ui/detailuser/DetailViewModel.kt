package com.dicoding.githubusersapp.ui.detailuser

import android.app.Application
import androidx.lifecycle.*
import com.dicoding.githubusersapp.R
import com.dicoding.githubusersapp.common.DataState
import com.dicoding.githubusersapp.common.ServiceBuilder
import com.dicoding.githubusersapp.model.UserDetail
import com.dicoding.githubusersapp.model.Users
import com.dicoding.githubusersapp.network.users.IUsers
import com.dicoding.githubusersapp.repository.favorite_users.FavoriteUsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DetailViewModel(app:Application): AndroidViewModel(app) {
    private val detailUser = MutableLiveData<UserDetail>()
    private val catchErrorMessage = MutableLiveData<String>()
    val getCatchErrorMessage:LiveData<String> get() = catchErrorMessage
    private val request = ServiceBuilder.buildService(IUsers::class.java)
    private val favoriteUsersRepository:FavoriteUsersRepository = FavoriteUsersRepository(app)
    private var users = Users()
    private var isFavorite = false

    fun sendUsers(user:Users){
       this.users = user
    }

    fun setIsFavorite(isFavorit:Boolean?){
        isFavorite = isFavorit?:false
    }
    fun getDetailUser(){
        try{
            viewModelScope.launch {
                val username = users.username
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

    fun checkUserAreFavorite(): LiveData<DataState<Boolean>> = favoriteUsersRepository.countFavorite(users.id)

    fun crdData() = liveData(Dispatchers.IO){
        try{
            val action:String = if(!isFavorite){
                emit(DataState.loading(
                    null,
                    getApplication<Application>().resources.getString(R.string.loading_insert)
                ))
                favoriteUsersRepository.insertData(users)
                DetailActivity.IS_SAVE
            }else{
                emit(DataState.loading(
                    null,
                    getApplication<Application>().resources.getString(R.string.loading_delete)
                ))
                favoriteUsersRepository.deleteData(users)
                DetailActivity.IS_DELETE
            }
            emit(DataState.success(action))
        }catch (e:Exception){
            emit(DataState.error(false,e.localizedMessage?:"uknown error occurred"))
        }
    }
}