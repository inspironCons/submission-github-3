package com.dicoding.githubusersapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Users(
    var id:Int = 0,
    var username:String ="",
    var avatar:String ="",
):Parcelable

