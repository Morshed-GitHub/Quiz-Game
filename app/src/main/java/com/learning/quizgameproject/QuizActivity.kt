package com.learning.quizgameproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.database.*
import com.learning.quizgameproject.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {
    lateinit var quizBinding: ActivityQuizBinding
    private val db : FirebaseDatabase = FirebaseDatabase.getInstance()
    private val dbRef : DatabaseReference = db.reference.child("questions")

    var question : String = ""
    var optA : String = ""
    var optB : String = ""
    var optC : String = ""
    var optD : String = ""
    var answer : String = ""
    var questionCount : Int = 0
    var questionNumber : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizBinding = ActivityQuizBinding.inflate(layoutInflater)
        val view : View = quizBinding.root
        setContentView(view)

        gameLogic()

        quizBinding.buttonNext.setOnClickListener { gameLogic() }
        quizBinding.buttonFinish.setOnClickListener {  }

        quizBinding.tvOptA.setOnClickListener {  }
        quizBinding.tvOptB.setOnClickListener {  }
        quizBinding.tvOptC.setOnClickListener {  }
        quizBinding.tvOptD.setOnClickListener {  }
    }

    private fun gameLogic() {
        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                questionCount = snapshot.childrenCount.toInt()

                if (questionNumber <= questionCount) {
                    question = snapshot.child(questionNumber.toString()).child("q").value.toString()
                    optA = snapshot.child(questionNumber.toString()).child("a").value.toString()
                    optB = snapshot.child(questionNumber.toString()).child("b").value.toString()
                    optC = snapshot.child(questionNumber.toString()).child("c").value.toString()
                    optD = snapshot.child(questionNumber.toString()).child("d").value.toString()
                    answer = snapshot.child(questionNumber.toString()).child("answer").value.toString()

                    quizBinding.tvQuestion.text = question
                    quizBinding.tvOptA.text = optA
                    quizBinding.tvOptB.text = optB
                    quizBinding.tvOptC.text = optC
                    quizBinding.tvOptD.text = optD

                    quizBinding.progressBarQuiz.isVisible = false
                    quizBinding.linearLayoutInfo.isVisible = true
                    quizBinding.linearLayoutQuestion.isVisible = true
                    quizBinding.linearLayoutbutton.isVisible = true

                    questionNumber++
                } else {
                    Toast.makeText(applicationContext, "Congratulations!! you have successfully completed the Quiz Game ✔", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}