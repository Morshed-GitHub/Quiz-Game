package com.learning.quizgameproject

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.learning.quizgameproject.databinding.ActivityQuizBinding
import kotlin.random.Random

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
    var questionNumber : Int = 0

    var correctAnswerCount : Int = 0
    var wrongAnswerCount : Int = 0

    lateinit var timer: CountDownTimer
    private val totalTime : Long = 30000L // 1s -> 1000ms & L -> Long Data Type
    var timerContinue : Boolean = false
    var leftTime : Long = totalTime

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val user : FirebaseUser? = auth.currentUser
    private val scoreRef : DatabaseReference = db.reference.child("scores")

    var qIndexes : HashSet<Int> = HashSet<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizBinding = ActivityQuizBinding.inflate(layoutInflater)
        val view : View = quizBinding.root
        setContentView(view)

        do {
            val randomIndex : Int = Random.nextInt(1,11)
            qIndexes.add(randomIndex)
        } while (qIndexes.size < 5)

        gameLogic()

        quizBinding.buttonNext.setOnClickListener {
            // If we don't reset the timer, then it will start from where it left from previous question
            resetTimer()
            gameLogic()
        }

        quizBinding.buttonFinish.setOnClickListener {
            sendScoreToFirebaseDB()
        }

        quizBinding.tvOptA.setOnClickListener {
            showQuestionResult("a", quizBinding.tvOptA)
        }
        quizBinding.tvOptB.setOnClickListener {
            showQuestionResult("b", quizBinding.tvOptB)
        }
        quizBinding.tvOptC.setOnClickListener {
            showQuestionResult("c", quizBinding.tvOptC)
        }
        quizBinding.tvOptD.setOnClickListener {
            showQuestionResult("d", quizBinding.tvOptD)
        }
    }

    private fun sendScoreToFirebaseDB() {
        user?.let {
            val userUID = it.uid
            scoreRef.child(userUID).child("correct").setValue(correctAnswerCount)
            scoreRef.child(userUID).child("wrong").setValue(wrongAnswerCount)

            Toast.makeText(applicationContext, "Scores saved successfully ✔", Toast.LENGTH_SHORT).show()

            val intent : Intent = Intent(this@QuizActivity, ResultActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showQuestionResult(ans: String, tvOpt: TextView) {
        pauseTimer()
        if (answer == ans) {
            tvOpt.setBackgroundColor(Color.GREEN)
            correctAnswerCount++
            quizBinding.tvCorrectAnswer.text = correctAnswerCount.toString()
        } else {
            tvOpt.setBackgroundColor(Color.RED)
            wrongAnswerCount++
            quizBinding.tvWrongAnswer.text = wrongAnswerCount.toString()
            findCorrectAnswer()
        }

        setClickableOptions(false)
    }

    private fun setClickableOptions(value : Boolean) {
        quizBinding.tvOptA.isClickable = value
        quizBinding.tvOptB.isClickable = value
        quizBinding.tvOptC.isClickable = value
        quizBinding.tvOptD.isClickable = value
    }

    private fun findCorrectAnswer() {
        when(answer) {
            "a" -> quizBinding.tvOptA.setBackgroundColor(Color.GREEN)
            "b" -> quizBinding.tvOptB.setBackgroundColor(Color.GREEN)
            "c" -> quizBinding.tvOptC.setBackgroundColor(Color.GREEN)
            "d" -> quizBinding.tvOptD.setBackgroundColor(Color.GREEN)
        }
    }

    private fun restoreOptions () {
        quizBinding.tvOptA.setBackgroundColor(Color.WHITE)
        quizBinding.tvOptB.setBackgroundColor(Color.WHITE)
        quizBinding.tvOptC.setBackgroundColor(Color.WHITE)
        quizBinding.tvOptD.setBackgroundColor(Color.WHITE)

        setClickableOptions(true)
    }

    private fun gameLogic() {
        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                questionCount = snapshot.childrenCount.toInt()

                if (questionNumber < qIndexes.size) {
                    restoreOptions() // Clear's previous answer's and restore everything as new
                    val ind : String = qIndexes.elementAt(questionNumber).toString()
                    question = snapshot.child(ind).child("q").value.toString()
                    optA = snapshot.child(ind).child("a").value.toString()
                    optB = snapshot.child(ind).child("b").value.toString()
                    optC = snapshot.child(ind).child("c").value.toString()
                    optD = snapshot.child(ind).child("d").value.toString()
                    answer = snapshot.child(ind).child("answer").value.toString()

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
                    startTimer()
                } else {
                    showDialogMessage()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showDialogMessage() {
        val dialogMessage = android.app.AlertDialog.Builder(this@QuizActivity)
        dialogMessage.setTitle("Quiz Game")
            .setMessage("Congratulations!!!\nYou have answered all the questions. Do you want to see the results?")
            .setCancelable(false) // AlertDialog will not be effected when user tapped outside of the AlertDialog
            .setPositiveButton("See Result") { dialogWindow, position ->
                sendScoreToFirebaseDB()
            }.setNegativeButton("Play Again") { dialogWindow, position ->
                val intent : Intent = Intent(this@QuizActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }.create().show()
    }

    private fun startTimer () {
        val oneSec : Long = 1000L

        // As CountDownTimer is an Abstract class, direct object/ instance creation is not possible.
        // We must have to create it's object as anonymous.
        timer = object : CountDownTimer(leftTime, oneSec) {
            override fun onTick(leftTimeInMillis: Long) {
                leftTime = leftTimeInMillis
                updateCountDownText()
            }

            override fun onFinish() {
                setClickableOptions(false)
                resetTimer()
                updateCountDownText()
                quizBinding.tvQuestion.text = "Sorry, time is up! Continue with next question."
                timerContinue = false
            }
        }.start()
        timerContinue = true
    }

    private fun resetTimer() {
        pauseTimer()
        leftTime = totalTime
        updateCountDownText()
    }

    private fun pauseTimer () {
        timer.cancel()
        timerContinue = false
    }

    private fun updateCountDownText() {
        val remainingTime : Int = (leftTime / 1000).toInt() // Get remainingTime in seconds
        quizBinding.tvTime.text = remainingTime.toString()
    }
}