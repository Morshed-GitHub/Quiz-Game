package com.learning.quizgameproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.learning.quizgameproject.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    lateinit var signupBinding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signupBinding = ActivitySignupBinding.inflate(layoutInflater)
        val view : View = signupBinding.root
        setContentView(view)

        signupBinding.buttonSignInsignup.setOnClickListener {  }
    }
}