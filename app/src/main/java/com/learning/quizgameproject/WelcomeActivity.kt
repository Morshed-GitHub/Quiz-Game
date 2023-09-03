package com.learning.quizgameproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatDelegate
import com.learning.quizgameproject.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    lateinit var splashBinding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        splashBinding = ActivityWelcomeBinding.inflate(layoutInflater)
        val view : View = splashBinding.root
        setContentView(view)

        val alphaAnimation : Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.splash_anim) // context, animation_id
        splashBinding.tvSplash.startAnimation(alphaAnimation)

        // In order to handle time delaying
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({ // object : Runnable {} -> Short
            val intent : Intent = Intent(this@WelcomeActivity, MainActivity::class.java)
            startActivity(intent)
            finish() // Destroy the current activity
        }, 4000) // 5000 milliseconds -> 5 seconds
    }
}