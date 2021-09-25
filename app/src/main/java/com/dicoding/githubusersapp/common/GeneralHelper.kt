package com.dicoding.githubusersapp.common

import android.view.View
import android.widget.ProgressBar
import com.dicoding.githubusersapp.component.EmptyState

object GeneralHelper {
    fun showLoading(show:Boolean,view:ProgressBar?){
        if(show){
            view?.visibility = View.VISIBLE
        }else{
            view?.visibility = View.GONE
        }
    }

    fun showEmptyState(state:Boolean,view:EmptyState?){
        if(state){
            view?.visibility = View.VISIBLE
        }else{
            view?.visibility = View.GONE
        }
    }

}