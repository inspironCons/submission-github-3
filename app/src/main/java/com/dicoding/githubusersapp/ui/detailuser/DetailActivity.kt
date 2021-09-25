package com.dicoding.githubusersapp.ui.detailuser

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.githubusersapp.R
import com.dicoding.githubusersapp.common.ViewModelFactory
import com.dicoding.githubusersapp.databinding.ActivityDetailBinding
import com.dicoding.githubusersapp.ui.detailuser.tabs.ShareViewModel
import com.dicoding.githubusersapp.ui.detailuser.tabs.TabsAdapter
import com.dicoding.githubusersapp.ui.home.HomeActivity
import com.google.android.material.tabs.TabLayoutMediator


class DetailActivity : AppCompatActivity() {
    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: DetailViewModel
    private lateinit var shareViewModel: ShareViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        viewModel = obtainViewModelDetail(this)
        shareViewModel = obtainShareViewModel(this)

        showData()
        getData()
        showTabs()
        onBackNav()

        viewModel.getCatchErrorMessage.observe(this){ error->
            Toast.makeText(this,error,Toast.LENGTH_LONG).show()
        }
    }

    private fun getData() {
        val intent = intent.extras
        if(intent != null){
            val username = intent.getString(HomeActivity.EXTRA_USERNAME) as String
            viewModel.getDetailUser(username)
        }
    }

    private fun showData() {
        binding?.apply {
            viewModel.getDataDetail().observe(this@DetailActivity){ detail->
                if(detail != null){
                    Glide.with(this@DetailActivity)
                        .load(detail.avatar)
                        .into(avatarProfile)
                    tvUsername.text = detail.username
                    tvRealName.text = detail.name
                    tvRepoValue.text = detail.repository.toString()
                    tvLocationValue.text = detail.location
                    tvCompanyValue.text = detail.company
                    shareViewModel.sendExtra(detail.username)
                    TabLayoutMediator(tabsFollows,viewFollows){ tab,position->
                        when(position){
                            0->tab.text = resources.getString(R.string.follower,detail.follower?:"0")
                            1->tab.text = resources.getString(R.string.following,detail.follower?:"0")
                        }
                    }.attach()
                }
            }
        }
    }

    private fun showTabs(){
        val tabsAdapter = TabsAdapter(this)
        binding?.viewFollows?.adapter = tabsAdapter
    }

    private fun onBackNav() {
        binding?.btnBack?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun obtainViewModelDetail(activity:AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity,factory).get(DetailViewModel::class.java)
    }

    private fun obtainShareViewModel(activity:AppCompatActivity): ShareViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity,factory).get(ShareViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}