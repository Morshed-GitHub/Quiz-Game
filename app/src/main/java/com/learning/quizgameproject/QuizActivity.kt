package com.learning.quizgameproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.learning.quizgameproject.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {
    lateinit var quizBinding: ActivityQuizBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizBinding = ActivityQuizBinding.inflate(layoutInflater)
        val view : View = quizBinding.root
        setContentView(view)

        quizBinding.buttonNext.setOnClickListener {  }
        quizBinding.buttonFinish.setOnClickListener {  }

        quizBinding.tvOptA.setOnClickListener {  }
        quizBinding.tvOptB.setOnClickListener {  }
        quizBinding.tvOptC.setOnClickListener {  }
        quizBinding.tvOptD.setOnClickListener {  }
    }
}