package com.learning.quizgameproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.learning.quizgameproject.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var loginBinding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view : View = loginBinding.root
        setContentView(view)

        loginBinding.tvSignUp.setOnClickListener {
            val intent : Intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}