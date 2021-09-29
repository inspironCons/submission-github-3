package com.dicoding.githubusersapp.ui.home

import androidx.lifecycle.*
import com.dicoding.githubusersapp.common.ServiceBuilder
import com.dicoding.githubusersapp.common.SettingsPreferences
import com.dicoding.githubusersapp.model.Users
import com.dicoding.githubusersapp.network.users.IUsers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val preferences: SettingsPreferences
):ViewModel() {
    private val listUsers = MutableLiveData<ArrayList<Users>>()
    private val catchErrorMessage = MutableLiveData<String>()
    val getCatchErrorMessage:LiveData<String> get() = catchErrorMessage
    private val request = ServiceBuilder.buildService(IUsers::class.java)
    private var isDarkMode = false

    fun getUsers(search:String?){
        try {
            if (search != null) {
                viewModelScope.launch {
                    val getData = request.searchUser(search)
                    val data = getData.users
                    val listItem = ArrayList<Users>()
                    for(i in data.indices){
                        val users = Users()
                        users.id = data[i].id
                        users.username = data[i].login
                        users.avatar = data[i].avatarUrl
                        listItem.add(users)
                    }
                    listUsers.postValue(listItem)
                }
            }
        }catch (e:Exception){
            catchErrorMessage.postValue(e.message)
        }
    }
    fun getUsersList():LiveData<ArrayList<Users>>{
        return listUsers
    }


    fun getThemeSettings(): LiveData<Boolean> {
        return preferences.getThemeSetting().asLiveData()
    }

    fun sendThemeSetting() {
        viewModelScope.launch {
            preferences.saveThemeSetting(isDarkMode)
        }
    }

    fun sendMode(darkMode:Boolean){
        isDarkMode = darkMode
    }
}