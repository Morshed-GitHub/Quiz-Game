package com.learning.quizgameproject

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.learning.quizgameproject.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var loginBinding: ActivityLoginBinding
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var arl : ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view : View = loginBinding.root
        setContentView(view)

        // Registration
        registerActivityResultLauncherForGoogleSignIn()

        // Customize Google SignIn Button
        val textOfGoogleButton : TextView = loginBinding.buttonGoogleSignIn.getChildAt(0) as TextView
        textOfGoogleButton.text = "Continue with Google"
        textOfGoogleButton.setTextColor(Color.BLACK)
        textOfGoogleButton.textSize = 18F // F -> Float

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


        loginBinding.buttonGoogleSignIn.setOnClickListener {
            // Note: We need to add Google Sign In Provider in Firebase Project
            continueWithGoogle()
        }
    }

    private fun registerActivityResultLauncherForGoogleSignIn() {
        arl = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
                ActivityResultCallback { result ->
                    val resultCode = result.resultCode
                    val resultData = result.data

                    if (resultCode == RESULT_OK && resultData != null) {
                        // Creates a thread using task class. This thread will get the data from Google in background
                        val task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(resultData)
                        firebaseSignInWithGoogle(task)
                    }
                }
            )
    }

    private fun firebaseSignInWithGoogle(task: Task<GoogleSignInAccount>) {
        try {
            val account : GoogleSignInAccount = task.getResult(ApiException::class.java)
            Toast.makeText(applicationContext, "Welcome to the Quiz Game ✔", Toast.LENGTH_SHORT).show()
            val intent : Intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
            firebaseGoogleAccount(account)
        } catch (e : ApiException) {
            Toast.makeText(applicationContext, e.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun firebaseGoogleAccount(account: GoogleSignInAccount) {
        val authCredential = GoogleAuthProvider.getCredential(account.idToken, null)
        // ID token is a unique code for each device & this helps us to know which device is logged in.
        // We register the authenticated device of the person logging into the system with the token ID.

        auth.signInWithCredential(authCredential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
//                val user = auth.currentUser
//                if (user != null) {
//                    println(user.email)
//                    println(user.displayName)
//                    println(user.photoUrl)
//                    println(user.phoneNumber)
//                }
            } else {

            }
        }
    }

    private fun continueWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                  // This method specified that an ID token for authenticate users is
                  // requested. Requesting an ID token requires that the server client
                  // ID be specified.
                  .requestIdToken("243525607958-optba77nonlbi3111705ht5a2ucmvv8i.apps.googleusercontent.com")
                  // Got this from Project Level -> app -> build -> generated -> res -> google-services
                  // -> debug -> values -> values.xml -> "default_web_client_id"
                  .requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        signIn()
    }

    private fun signIn() {
        val signInIntent : Intent = googleSignInClient.signInIntent
        arl.launch(signInIntent)
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