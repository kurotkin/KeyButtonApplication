package com.kurotkin.keybuttonapplication

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.kurotkin.keybuttonapplication.mail.MailSender
import com.ncorti.slidetoact.SlideToActView
import com.ncorti.slidetoact.SlideToActView.OnSlideCompleteListener
import kotlinx.android.synthetic.main.activity_alarm.*
import java.util.*


class AlarmActivity : AppCompatActivity() {

    private lateinit var timerValue: TextView
    private val t = 54_000L

    private val startTime = Date().time

    private val customHandler = Handler()
    private var timeInMilliseconds = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        timerValue = findViewById(R.id.calc_time)

        val cl = findViewById<ConstraintLayout>(R.id.layoutAnimatedBackground)
        cl.setBackgroundResource(R.drawable.animate)
        val ad = cl.background as AnimationDrawable
        ad.start()

        val sl  = object : OnSlideCompleteListener {
            override fun onSlideComplete(view: SlideToActView) {
                finish()
            }
        }

        example_gray_on_green.onSlideCompleteListener = sl
    }

    override fun onStart() {
        super.onStart()

        MailSender.sendEmail()

        //customHandler.postDelayed(updateTimerThread, 0)
    }

    private val updateTimerThread: Runnable = object : Runnable {
        override fun run() {
            timeInMilliseconds = Date().time - startTime
            var secs = ((t - timeInMilliseconds) / 1000).toInt()
            secs = secs % 60
            timerValue.text = String.format("%01d", secs)

            if (secs < 4) {
                vibro(400)
            } else {
                vibro(100)
            }

            if(secs == 0) {
                vibro(2000)
                Toast.makeText(this@AlarmActivity, "Ваше время истекло", Toast.LENGTH_LONG).show()
                finish()
            }
            customHandler.postDelayed(this, 1000)
        }
    }

    private fun vibro(t: Long = 200){
        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(t, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            v.vibrate(t)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        customHandler?.removeCallbacks(updateTimerThread)
    }
}