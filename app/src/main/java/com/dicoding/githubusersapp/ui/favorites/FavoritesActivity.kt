package com.dicoding.githubusersapp.ui.favorites

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.githubusersapp.adapter.MainAdapter
import com.dicoding.githubusersapp.common.GeneralHelper.showEmptyState
import com.dicoding.githubusersapp.common.GeneralHelper.showLoading
import com.dicoding.githubusersapp.common.State
import com.dicoding.githubusersapp.common.ViewModelFactory
import com.dicoding.githubusersapp.databinding.ActivityFavoritesBinding

class FavoritesActivity:AppCompatActivity() {
    private var _binding: ActivityFavoritesBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: FavoritesViewModel
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        viewModel = obtainViewModel(this)
        setListFavorites()
    }

    private fun setListFavorites(){
        binding?.apply {
            val adapter = MainAdapter()
            rvListFavorites.layoutManager = GridLayoutManager(this@FavoritesActivity, 2)
            rvListFavorites.adapter = adapter
            viewModel.getAllDataFavorites().observe(this@FavoritesActivity,{ result->
                if(result.state == State.LOADING){
                    showEmptyState(false,viewEmptyState)
                    showLoading(true,loader)
                }

                if(result.state == State.ERROR){
                    showToas(result.message)
                    adapter.setItem(emptyList())
                    showLoading(false,loader)
                    showEmptyState(true,viewEmptyState)
                }

                if(result.state == State.SUCCESS){
                    val data = result?.data
                    if (data != null) {
                        showEmptyState(false,viewEmptyState)
                        showLoading(false,loader)
                        if(result.data.isNotEmpty()){
                            adapter.setItem(result.data)
                        }else{
                            showEmptyState(true,viewEmptyState)
                        }
                    }
                }
            })
        }
    }

    private fun obtainViewModel(activity:AppCompatActivity): FavoritesViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity,factory).get(FavoritesViewModel::class.java)
    }

    private fun showToas(msg:String?){
        toast?.cancel()
        toast = Toast.makeText(this,msg,Toast.LENGTH_LONG)
        toast?.show()
        showLoading(false,binding?.loader)

    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}