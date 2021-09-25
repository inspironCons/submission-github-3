package com.dicoding.githubusersapp.ui.detailuser.tabs

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabsAdapter(activity: AppCompatActivity):FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        return ListFollowsFragment.newInstance(position)
    }

    override fun getItemCount(): Int = 2
}