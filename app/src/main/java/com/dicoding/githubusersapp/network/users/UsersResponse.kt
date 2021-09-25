package com.dicoding.githubusersapp.network.users

import com.google.gson.annotations.SerializedName

data class UsersSearchResponse(
    @SerializedName("items") var users : List<Users>
)

data class Users(
    @SerializedName("login") var login : String,
    @SerializedName("avatar_url") var avatarUrl : String,
)

data class UserDetailResponse(
    @SerializedName("login") var login : String,
    @SerializedName("avatar_url") var avatarUrl : String,
    @SerializedName("name") var name : String,
    @SerializedName("company") var company : String,
    @SerializedName("location") var location : String,
    @SerializedName("public_repos") var publicRepos : Int,
    @SerializedName("public_gists") var publicGists : Int,
    @SerializedName("followers") var followers : Int,
    @SerializedName("following") var following : Int,
)