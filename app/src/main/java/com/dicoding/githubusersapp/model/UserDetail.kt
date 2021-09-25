package com.dicoding.githubusersapp.model

data class UserDetail(
    var id:Int? = 0,
    var username:String? = null,
    var name:String? = null,
    var avatar:String? = null,
    var company:String? = null,
    var location:String? = null,
    var repository:Int? = null,
    var follower:Int? = null,
    var following:Int? = null
)