package com.dicoding.githubusersapp.ui.detailuser.tabs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.githubusersapp.common.ServiceBuilder
import com.dicoding.githubusersapp.model.Users
import com.dicoding.githubusersapp.network.users.IUsers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ShareViewModel:ViewModel() {
    val username = MutableLiveData<String>()
    private val _catchErrorMessage = MutableLiveData<String>()
    val catchErrorMessage:LiveData<String> get()= _catchErrorMessage
    private val listFollowing = MutableLiveData<ArrayList<Users>>()
    private val listFollowers = MutableLiveData<ArrayList<Users>>()

    private val request = ServiceBuilder.buildService(IUsers::class.java)

    fun sendExtra(user:String?){
        username.value = user as String
    }

    fun getListFollowing(username: String) {
        try{
            viewModelScope.launch {
                val user = request.getFollowingUser(username)
                val listItem = ArrayList<Users>()
                for(i in user.indices){
                    val userDetail = Users()
                    userDetail.username = user[i].login
                    userDetail.avatar = user[i].avatarUrl
                    listItem.add(userDetail)
                }
                listFollowing.postValue(listItem)
            }
        }catch (e:HttpException){
            _catchErrorMessage.postValue("${e.message()} - ${e.code()}")
        }catch (e:Exception){
            _catchErrorMessage.postValue(e.localizedMessage)
        }
    }
    fun showListFollowing():LiveData<ArrayList<Users>>{
        return listFollowing
    }

    fun getListFollowers(username: String) {
        try{
            viewModelScope.launch {
                val user = request.getFollowerUser(username)
                val listItem = ArrayList<Users>()
                for(i in user.indices){
                    val userDetail = Users()
                    userDetail.username = user[i].login
                    userDetail.avatar = user[i].avatarUrl
                    listItem.add(userDetail)
                }
                listFollowers.postValue(listItem)
            }
        }catch (e:HttpException){
            _catchErrorMessage.postValue("${e.message()} - ${e.code()}")
        }catch (e:Exception){
            _catchErrorMessage.postValue(e.localizedMessage)
        }
    }
    fun showListFollowers():LiveData<ArrayList<Users>>{
        return listFollowers
    }
}