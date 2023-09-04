package com.learning.quizgameproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.learning.quizgameproject.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var mainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view : View = mainBinding.root
        setContentView(view)

        mainBinding.buttonSignOut.setOnClickListener {
            // Email & Password Login Case
            FirebaseAuth.getInstance().signOut()

            // Google Sign In Case
            val gso : GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build()
            val googleSignInClient : GoogleSignInClient = GoogleSignIn.getClient(this, gso)
            googleSignInClient.signOut().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                  Toast.makeText(applicationContext, "Successfully logged out ✔", Toast.LENGTH_SHORT).show()
                }
            }

            val intent : Intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        mainBinding.buttonStartQuiz.setOnClickListener {
            val intent : Intent = Intent(this@MainActivity, QuizActivity::class.java)
            startActivity(intent)
        }
    }
}