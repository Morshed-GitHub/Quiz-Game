package com.learning.quizgameproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.learning.quizgameproject.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    lateinit var signupBinding: ActivitySignupBinding
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signupBinding = ActivitySignupBinding.inflate(layoutInflater)
        val view : View = signupBinding.root
        setContentView(view)

        signupBinding.buttonSignInsignup.setOnClickListener {
            val email : String = signupBinding.etSignupEmail.text.toString()
            val password : String = signupBinding.etSignupPassword.text.toString()
            signUpWithFirebase(email, password)
        }
    }

    private fun signUpWithFirebase(email: String, password: String) {
        signupBinding.progressBar.isVisible = true
        signupBinding.buttonSignInsignup.isClickable = false

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(applicationContext, "You account has been created ✔", Toast.LENGTH_SHORT).show()
                finish() // Close the current activity and get back to login screen
            } else {
                Toast.makeText(applicationContext, task.exception!!.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
            }
            signupBinding.progressBar.isVisible = false
            signupBinding.buttonSignInsignup.isClickable = true
        }
    }
}