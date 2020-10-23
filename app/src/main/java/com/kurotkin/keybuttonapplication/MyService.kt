package com.kurotkin.keybuttonapplication

import android.app.ActionBar
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.WindowManager.LayoutParams
import android.widget.ImageView
import androidx.annotation.RequiresApi


class MyService : Service() {
    private lateinit var windowManager: WindowManager
    private lateinit var floatingFaceBubble: ImageView

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onCreate() {
        super.onCreate()
        floatingFaceBubble = ImageView(this)
        floatingFaceBubble.setImageResource(
            resources.getIdentifier(
                "img_alarm",
                "drawable",
                packageName
            )
        )
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val myParams = WindowManager.LayoutParams(
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.WRAP_CONTENT,
            LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        myParams.gravity = Gravity.TOP or Gravity.LEFT
        myParams.x = 850
        myParams.y = 50
        windowManager.addView(floatingFaceBubble, myParams)
        try {
            floatingFaceBubble.setOnTouchListener(object : View.OnTouchListener {
                var paramsT: WindowManager.LayoutParams = myParams
                private var initialX = 0
                private var initialY = 0
                private var initialTouchX = 0f
                private var initialTouchY = 0f
                private var touchStartTime: Long = 0
                override fun onTouch(v: View?, event: MotionEvent): Boolean {
                    //remove face bubble on long press
//                    if (System.currentTimeMillis() - touchStartTime > ViewConfiguration.getLongPressTimeout() && initialTouchX == event.x) {
//                        windowManager.removeView(floatingFaceBubble)
//                        stopSelf()
//                        return false
//                    }
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            touchStartTime = System.currentTimeMillis()
                            initialX = myParams.x
                            initialY = myParams.y
                            initialTouchX = event.rawX
                            initialTouchY = event.rawY
                        }
                        MotionEvent.ACTION_UP -> {
                        }
                        MotionEvent.ACTION_MOVE -> {
                            myParams.x = initialX + (event.rawX - initialTouchX).toInt()
                            myParams.y = initialY + (event.rawY - initialTouchY).toInt()
                            windowManager.updateViewLayout(v, myParams)
                        }
                    }
                    return false
                }

            })
            floatingFaceBubble.setOnLongClickListener {
                startActivity()
                true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startActivity(){
        if((applicationContext as App).isTargetActivityStarted) {
        } else {
            Log.e("TAG", "start Activity")

            val intent = Intent(application, AlarmActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            //intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }
    }

//    override fun onCreate() {
//        super.onCreate()
//        val manager = getSystemService (WINDOW_SERVICE) as WindowManager
//        val params = WindowManager.LayoutParams(
//            50, // Ширина экрана
//        50, // Высота экрана
//        WindowManager.LayoutParams.TYPE_PHONE, // Говорим, что приложение будет поверх других. В поздних API > 26, данный флаг перенесен на TYPE_APPLICATION_OVERLAY
//        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, // Необходимо для того чтобы TouchEvent'ы в пустой области передавались на другие приложения
//        PixelFormat.TRANSLUCENT) // Само окно прозрачное
//
//        // Задаем позиции для нашего Layout
//        params.gravity = Gravity.TOP
//        params.x = 0
//        params.y = 0
//
//        // Отображаем наш Layout
//        val rootView = LayoutInflater.from(this).inflate(R.layout.icon, null)
//        manager.addView(rootView, params)
//
//    }

}