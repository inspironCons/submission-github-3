package com.dicoding.githubusersapp.helper

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.githubusersapp.model.Users

class UsersDiffCallback(
    private val oldList:List<Users>,
    private val newList:List<Users>
):DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]
        return old.avatar == new.avatar && new.username == new.username
    }
}