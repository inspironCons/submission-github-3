package com.dicoding.githubusersapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubusersapp.databinding.ItemFollowsBinding
import com.dicoding.githubusersapp.model.Users

class ListFollowsAdapter:RecyclerView.Adapter<ListFollowsAdapter.ViewHolder>() {
    private var listFollows:List<Users> = listOf()
    inner class ViewHolder(view: ItemFollowsBinding):RecyclerView.ViewHolder(view.root){
        val username = view.tvUsername
        val avatar = view.avatarProfile
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFollowsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listFollows[position]
        Glide.with(holder.itemView.context)
            .load(item.avatar)
            .into(holder.avatar)
        holder.username.text = item.username
    }

    override fun getItemCount(): Int = listFollows.size

    fun setItem(items:List<Users>){
        listFollows = items
        notifyDataSetChanged()
    }
}