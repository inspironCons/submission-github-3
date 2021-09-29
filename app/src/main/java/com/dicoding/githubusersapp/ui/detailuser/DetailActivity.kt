package com.dicoding.githubusersapp.ui.detailuser

import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.githubusersapp.R
import com.dicoding.githubusersapp.common.State
import com.dicoding.githubusersapp.common.ViewModelFactory
import com.dicoding.githubusersapp.databinding.ActivityDetailBinding
import com.dicoding.githubusersapp.model.Users
import com.dicoding.githubusersapp.ui.detailuser.tabs.ShareViewModel
import com.dicoding.githubusersapp.ui.detailuser.tabs.TabsAdapter
import com.google.android.material.tabs.TabLayoutMediator


class DetailActivity : AppCompatActivity() {
    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: DetailViewModel
    private val shareViewModel: ShareViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        viewModel = obtainViewModelDetail(this)
        getData()
        showData()
        checkThisAccountAreFavorite()
        showTabs()
        clickFavorite()
        onBackNav()

        viewModel.getCatchErrorMessage.observe(this){ error->
            Toast.makeText(this,error,Toast.LENGTH_LONG).show()
        }

    }

    private fun checkThisAccountAreFavorite(){
        viewModel.checkUserAreFavorite().observe(this,{ state->
            if(state.state == State.SUCCESS){
                if(state.data == true){
                    binding?.fabFavorite?.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this,R.color.pink))
                }else{
                    binding?.fabFavorite?.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this,R.color.white))
                }
                viewModel.setIsFavorite(state.data)
            }

            if(state.state == State.ERROR){
                binding?.fabFavorite?.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this,R.color.white))
                Toast.makeText(this@DetailActivity,state.message,Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getData() {
        val intent = intent.extras
        if(intent != null){
            val user = intent.getParcelable<Users>(EXTRA_USERS) as Users
            viewModel.sendUsers(user)
            viewModel.getDetailUser()
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

    private fun clickFavorite(){
        binding?.fabFavorite?.setOnClickListener {
            checkThisAccountAreFavorite()
            viewModel.crdData().observe(this,{
                var toast: Toast? = null
                if(it.state == State.SUCCESS){
                    toast?.cancel()
                    toast = Toast.makeText(this,resources.getString(R.string.success),Toast.LENGTH_SHORT)
                    toast?.show()
                    if(it.data == IS_SAVE) binding?.fabFavorite?.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this,R.color.pink))
                    if(it.data == IS_DELETE) binding?.fabFavorite?.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this,R.color.white
                    ))
                }
                if(it.state == State.LOADING){
                    toast = Toast.makeText(this,it.message,Toast.LENGTH_LONG)
                    toast?.show()
                }

                if(it.state == State.ERROR){
                    toast = Toast.makeText(this,it.message,Toast.LENGTH_LONG)
                    toast?.show()
                }

            })
        }
    }

    private fun obtainViewModelDetail(activity:AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity,factory).get(DetailViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        const val EXTRA_USERS = "EXTRA_USERS"
        const val IS_SAVE = "IS_SAVE"
        const val IS_DELETE = "IS_DELETE"
    }
}