package com.learning.quizgameproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.learning.quizgameproject.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var forgotPasswordBinding : ActivityForgotPasswordBinding
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forgotPasswordBinding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view : View = forgotPasswordBinding.root
        setContentView(view)

        forgotPasswordBinding.buttonRecoveryPassword.setOnClickListener {
            val email : String = forgotPasswordBinding.etRecoveryEmail.text.toString()
            resetPassword(email)
        }
    }

    private fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(applicationContext, "We sent a password reset mail to $email ✔", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(applicationContext, task.exception!!.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}