package com.learning.quizgameproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.learning.quizgameproject.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    lateinit var resultBinding: ActivityResultBinding
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    val user : FirebaseUser? = auth.currentUser
    private val db : FirebaseDatabase = FirebaseDatabase.getInstance()
    private val scoreRef : DatabaseReference = db.reference.child("scores")

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.title = "Score"
        super.onCreate(savedInstanceState)
        resultBinding = ActivityResultBinding.inflate(layoutInflater)
        val view : View = resultBinding.root
        setContentView(view)

        resultBinding.buttonPlayAgain.setOnClickListener {
            val intent : Intent = Intent(this@ResultActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        resultBinding.buttonExit.setOnClickListener { finish() }

        scoreRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                user?.let {
                    resultBinding.tvResultCorrect.text = snapshot.child(it.uid).child("correct").value.toString()
                    resultBinding.tvResultWrong.text = snapshot.child(it.uid).child("wrong").value.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}