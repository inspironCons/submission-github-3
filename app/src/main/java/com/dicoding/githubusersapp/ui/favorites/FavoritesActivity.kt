package com.dicoding.githubusersapp.ui.favorites

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.githubusersapp.adapter.MainAdapter
import com.dicoding.githubusersapp.common.GeneralHelper.showEmptyState
import com.dicoding.githubusersapp.common.GeneralHelper.showLoading
import com.dicoding.githubusersapp.common.ViewModelFactory
import com.dicoding.githubusersapp.database.favorite_users.FavoriteUsersEntity
import com.dicoding.githubusersapp.databinding.ActivityFavoritesBinding
import com.dicoding.githubusersapp.model.Users
import com.dicoding.githubusersapp.ui.detailuser.DetailActivity

class FavoritesActivity:AppCompatActivity() {
    private var _binding: ActivityFavoritesBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: FavoritesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        viewModel = obtainViewModel(this)
        setListFavorites()
        onBackNav()
    }

    private fun setListFavorites(){
        binding?.apply {
            val adapter = MainAdapter()
            rvListFavorites.layoutManager = GridLayoutManager(this@FavoritesActivity, 2)
            rvListFavorites.adapter = adapter
            showLoading(true,binding?.loader)
            viewModel.getAllDataFavorites().observe(this@FavoritesActivity,{ favoriteList->
                showLoading(false,binding?.loader)
                if(!favoriteList.isNullOrEmpty()){
                    showEmptyState(false,binding?.viewEmptyState)
                    val mapList = favoriteList.map{it.toUsers()}
                    adapter.setItem(mapList)
                }else{
                    showEmptyState(true,binding?.viewEmptyState)
                    adapter.setItem(emptyList())
                }
            })

            adapter.setOnItemCallback(object:MainAdapter.OnItemCallback{
                override fun onItemClicked(user: Users) {
                    val mIntent = Intent(this@FavoritesActivity,DetailActivity::class.java)
                    mIntent.putExtra(DetailActivity.EXTRA_USERS,user)
                    startActivity(mIntent)
                }
            })
        }
    }

    private fun obtainViewModel(activity:AppCompatActivity): FavoritesViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity,factory).get(FavoritesViewModel::class.java)
    }


    private fun onBackNav() {
        binding?.btnBack?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun FavoriteUsersEntity.toUsers():Users{
        val data = this
        return Users(
            id = data.id,
            username = data.username,
            avatar = data.avatar
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}