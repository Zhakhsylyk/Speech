package com.example.healthapp

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_exercise.*
import java.util.*

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private var tts: TextToSpeech? = null


    var i = 0
    var pictures = arrayOf(R.drawable.ic_abdominal_crunch,
            R.drawable.ic_high_knees_running_in_place,
            R.drawable.ic_jumping_jacks,
            R.drawable.ic_lunge,
            R.drawable.ic_plank,
            R.drawable.ic_push_up,
            R.drawable.ic_push_up_and_rotation,
            R.drawable.ic_side_plank,
            R.drawable.ic_squat,
            R.drawable.ic_step_up_onto_chair,
            R.drawable.ic_triceps_dip_on_chair,
            R.drawable.ic_wall_sit
                            )
    var names = arrayOf("abdominal crunch",
                        "high knees running in place",
                        "jumping jacks",
                        "lunge",
                        "plank",
                        "push up",
                        "push up and rotation",
                        "side plank",
                        "squat",
                        "step up onto chair",
                        "triceps dip on chair",
                        "wall sit"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)
        setSupportActionBar(toolbar_exercise_activity)
        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
        }
        toolbar_exercise_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        tts = TextToSpeech(this, this)

        setupRestView()

    }

    override fun onDestroy() {
        if (restTimer != null) {
            restTimer!!.cancel()
            restProgress = 0
        }
        super.onDestroy()
    }

    override fun onInit(status: Int) {

        if (status == TextToSpeech.SUCCESS) {

            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language specified is not supported!")
            }

        } else {
            Log.e("TTS", "Initialization Failed!")
        }
    }

    private fun setRestProgressBar() {
        progressBar.progress = restProgress
        exerName.text = "Upcoming exercise is " + names[i]
        var player = MediaPlayer.create(applicationContext, R.raw.press_start)
        player!!.isLooping = false
        player.start()
        restTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                progressBar.progress = 10 - restProgress
                tvTimer.text = (10 - restProgress).toString()
            }

            override fun onFinish() {
                Toast.makeText(
                        this@ExerciseActivity,
                        "here we start the exercise",
                        Toast.LENGTH_SHORT
                ).show()

                exerciseImage.visibility = VISIBLE
                exerciseImage.setImageResource(pictures[i])
                exerName.text = names[i]
                i = i + 1
                getReady.text = " "
                progressBar.max = 30
                setExerciseView()
            }
        }.start()

    }

    private fun setExerciseView() {

        restTimer!!.cancel()
        restProgress = 0
        var speech = exerName.text.toString()
        speakOut(speech)


        restTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                progressBar.progress = 30 - restProgress
                tvTimer.text = (30 - restProgress).toString()
            }

            override fun onFinish() {
                Toast.makeText(
                        this@ExerciseActivity,
                        "have a rest",
                        Toast.LENGTH_SHORT
                ).show()
                exerciseImage.visibility = INVISIBLE
                getReady.text = "GET READY FOR"
                progressBar.max = 10

                setupRestView()
            }
        }.start()
    }

    private fun setupRestView() {
        if (restTimer!=null) {
            restTimer!!.cancel()
            restProgress = 0
        }
        setRestProgressBar()
    }

    private fun speakOut(text: String) {
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

}