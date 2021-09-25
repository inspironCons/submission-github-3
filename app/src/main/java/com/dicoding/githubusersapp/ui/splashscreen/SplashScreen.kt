package com.dicoding.githubusersapp.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.githubusersapp.databinding.ActivitySplashScreenBinding
import com.dicoding.githubusersapp.ui.home.HomeActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val delay = 4000L

        val animateLogo = binding.iconGif.animate()
        animateLogo.translationY(-1400f)
        animateLogo.duration = 1000
        animateLogo.startDelay = delay
        animateLogo.alpha(0f)
        val animateName = binding.tvAppName.animate()
        animateName.duration = 500
        animateName.startDelay = delay
        animateName.alpha(0f)


        Looper.myLooper()?.let {
            Handler(it).postDelayed({
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }, delay + 500)
        }

    }
}