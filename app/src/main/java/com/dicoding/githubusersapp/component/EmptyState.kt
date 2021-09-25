package com.dicoding.githubusersapp.component

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import com.dicoding.githubusersapp.R

class EmptyState @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
):LinearLayout(context, attrs, defStyle, defStyleRes) {
    init{
        LayoutInflater.from(context).inflate(R.layout.view_empty_state,this,true)
        orientation = VERTICAL
        gravity = Gravity.CENTER

        attrs?.let { attr->
            val typedArray = context.obtainStyledAttributes(attr,R.styleable.EmptyState,0,0)
            val title = typedArray.getString(R.styleable.EmptyState_title).orEmpty()
            val desc = typedArray.getString(R.styleable.EmptyState_description)
            val drawable = typedArray.getDrawable(R.styleable.EmptyState_image)
            this.findViewById<TextView>(R.id.tv_title).text = title
            this.findViewById<TextView>(R.id.tv_desc).text = desc

            if(drawable != null){
                this.findViewById<ImageView>(R.id.iv_empty).setImageDrawable(drawable)
            }else{
                this.findViewById<ImageView>(R.id.iv_empty).setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_empty_state))
            }

            typedArray.recycle()
        }
    }

    fun setDescription(desc:String){
        this.findViewById<TextView>(R.id.tv_desc).text = desc
    }
}