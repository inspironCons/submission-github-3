package com.dicoding.githubusersapp.ui.detailuser.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.githubusersapp.R
import com.dicoding.githubusersapp.adapter.ListFollowsAdapter
import com.dicoding.githubusersapp.common.GeneralHelper.showEmptyState
import com.dicoding.githubusersapp.common.GeneralHelper.showLoading
import com.dicoding.githubusersapp.databinding.FragmentFollowsBinding
import java.util.concurrent.Executors

class ListFollowsFragment: Fragment() {
    private var _binding: FragmentFollowsBinding? = null
    private val binding get() = _binding!!
    private val shareViewModel:ShareViewModel by activityViewModels()
    private lateinit var rvList:RecyclerView
    private lateinit var adapterList: ListFollowsAdapter
    private var toast:Toast? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val page = arguments?.getInt(ARG_SECTION_NUMBER,0) as Int
        activity?.let{activity->
            if(page == 1){
                binding.viewEmptyState.setDescription(activity.resources.getString(R.string.empty_list_desc_followings))
            }else{
                binding.viewEmptyState.setDescription(activity.resources.getString(R.string.empty_list_desc_followers))
            }
        }
        rvList = binding.rvListFollows
        rvList.layoutManager = LinearLayoutManager(context)
        adapterList = ListFollowsAdapter()
        val executors = Executors.newSingleThreadExecutor()
        showEmptyState(true, binding.viewEmptyState)
        shareViewModel.username.observe(viewLifecycleOwner,{ username->
            when(page){
                0->{
                    executors.execute{
                        shareViewModel.getListFollowers(username)
                    }
                    showDataFollowers()
                }
                1->{
                    executors.execute{
                        shareViewModel.getListFollowing(username)
                    }
                    showDataFollowing()
                }
            }
        })
        catchError()
    }

    private fun catchError() {
        shareViewModel.catchErrorMessage.observe(viewLifecycleOwner,{ msg->
            toast?.cancel()
            toast = Toast.makeText(context,msg,Toast.LENGTH_LONG)
            toast?.show()
            showLoading(false,binding.loader)
            showEmptyState(true, binding.viewEmptyState)
        })
    }

    private fun showDataFollowing() {
        showLoading(true,binding.loader)
        shareViewModel.showListFollowing().observe(viewLifecycleOwner,{ followings->
            showLoading(false,binding.loader)
            showEmptyState(false, binding.viewEmptyState)
            if(followings.isNotEmpty()){
                adapterList.setItem(followings)
            }else{
                showEmptyState(true, binding.viewEmptyState)
            }
            rvList.adapter = adapterList
        })
    }

    private fun showDataFollowers() {
        showLoading(true,binding.loader)
        shareViewModel.showListFollowers().observe(viewLifecycleOwner,{followers->
            showLoading(false,binding.loader)
            showEmptyState(false, binding.viewEmptyState)
            if(followers.isNotEmpty()){
                adapterList.setItem(followers)
            }else{
                showEmptyState(true,binding.viewEmptyState)
            }
            rvList.adapter = adapterList
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        private const val ARG_SECTION_NUMBER = "section_number"
        fun newInstance(index:Int) =
            ListFollowsFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_SECTION_NUMBER,index)
            }
        }
    }
}