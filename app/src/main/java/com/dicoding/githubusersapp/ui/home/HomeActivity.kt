package com.dicoding.githubusersapp.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.addTextChangedListener
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.githubusersapp.R
import com.dicoding.githubusersapp.adapter.MainAdapter
import com.dicoding.githubusersapp.common.GeneralHelper.showEmptyState
import com.dicoding.githubusersapp.common.GeneralHelper.showLoading
import com.dicoding.githubusersapp.common.SettingsPreferences
import com.dicoding.githubusersapp.common.ViewModelFactory
import com.dicoding.githubusersapp.databinding.ActivityHomeBinding
import com.dicoding.githubusersapp.model.Users
import com.dicoding.githubusersapp.ui.detailuser.DetailActivity
import com.dicoding.githubusersapp.ui.favorites.FavoritesActivity
import java.util.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class HomeActivity : AppCompatActivity() {
    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding
    private lateinit var rvList: RecyclerView
    private lateinit var adapterList: MainAdapter
    private lateinit var homeViewModel: HomeViewModel
    private var toast:Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        homeViewModel = obtainViewModelHome(this)
        rvList = binding?.rvList!!
        adapterList = MainAdapter()
        searchUsername()
        whenErrorSearch()
        clickItems()
        clickMenus()
        showRecycleView()
        themeChange()

        binding?.tvGreetings?.text = greeting()
    }

    private fun clickMenus() {
        binding?.btnMenu?.setOnClickListener { view:View->
            showMenu(view, R.menu.overflow_menu)
        }
    }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(this, v)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.language->{
                    val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                    startActivity(mIntent)
                }
                R.id.favorites->{
                    val mIntent = Intent(this@HomeActivity,FavoritesActivity::class.java)
                    startActivity(mIntent)
                }
            }
            true
        }
        popup.show()
    }

    private fun searchUsername() {
        binding?.searchBarValue?.addTextChangedListener { s->
            val search = s.toString()
            adapterList.setItem(emptyList())
            if(search.length >= 5){
                showLoading(true,binding?.loader)
                homeViewModel.getUsers(search)
            }else{
                showEmptyState(true,binding?.viewEmptyState)
            }
        }
    }

    private fun whenErrorSearch() {
        homeViewModel.getCatchErrorMessage.observe(this){msg->
            toast?.cancel()
            toast = Toast.makeText(this@HomeActivity,msg,Toast.LENGTH_LONG)
            toast?.show()
            showLoading(false,binding?.loader)
            showEmptyState(true,binding?.viewEmptyState)
        }
    }

    private fun showRecycleView() {
        rvList.layoutManager = GridLayoutManager(this, 2)
        homeViewModel.getUsersList().observe(this){ users->
            showEmptyState(false,binding?.viewEmptyState)
            if(users.isNotEmpty()){
                adapterList.setItem(users)
            }else{
                showEmptyState(true,binding?.viewEmptyState)
            }
            showLoading(false,binding?.loader)
        }
        rvList.adapter = adapterList
    }

    private fun clickItems() {
        adapterList.setOnItemCallback(object : MainAdapter.OnItemCallback {
            override fun onItemClicked(user: Users) {
                val mIntent = Intent(this@HomeActivity, DetailActivity::class.java)
                mIntent.putExtra(DetailActivity.EXTRA_USERS,user)
                startActivity(mIntent)
            }
        })
    }

    private fun greeting(): String {
        val calendar: Calendar = Calendar.getInstance()
        return when (calendar.get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> resources.getString(R.string.good_morning_reviewer)
            in 12..15 -> resources.getString(R.string.good_afternoon_reviewer)
            in 16..20 -> resources.getString(R.string.good_evening_reviewer)
            else -> resources.getString(R.string.good_night_reviewer)
        }
    }

    private fun obtainViewModelHome(activity:AppCompatActivity): HomeViewModel {
        val factory = ViewModelFactory.getInstance(
            activity.application,
            SettingsPreferences.getInstance(dataStore)
        )
        return ViewModelProvider(activity,factory).get(HomeViewModel::class.java)
    }

    private fun themeChange(){
        homeViewModel.getThemeSettings().observe(this,{ darkMode->
            if(darkMode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            homeViewModel.sendMode(!darkMode)
        })
        binding?.btnTheme?.setOnClickListener {
            homeViewModel.sendThemeSetting()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



}