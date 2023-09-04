package com.learning.quizgameproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.learning.quizgameproject.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var loginBinding: ActivityLoginBinding
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view : View = loginBinding.root
        setContentView(view)

        loginBinding.tvSignUp.setOnClickListener {
            val intent : Intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
        }

        loginBinding.buttonSignInsignup.setOnClickListener {
            val email : String = loginBinding.etLoginEmail.text.toString()
            val password : String = loginBinding.etLoginPassword.text.toString()
            signInWithFirebase(email, password)
        }

        loginBinding.tvForgotPassword.setOnClickListener {
            val intent : Intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        // Checking User Session
        val isUserLoggedIn : Boolean = auth.currentUser != null
        if (isUserLoggedIn) {
            navigateToMainActivity()
        }
    }

    private fun signInWithFirebase(email: String, password: String) {
        loginBinding.progressBar2.isVisible = true
        loginBinding.buttonSignInsignup.isClickable = false

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                navigateToMainActivity()
            } else {
                Toast.makeText(applicationContext, task.exception!!.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
            }
            loginBinding.progressBar2.isVisible = false
            loginBinding.buttonSignInsignup.isClickable = true
        }
    }

    private fun navigateToMainActivity() {
        Toast.makeText(applicationContext, "Welcome to the Quiz Game ✔", Toast.LENGTH_SHORT).show()
        val intent : Intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        finish() // Close the current activity and get back to login screen
    }
}