package com.kurotkin.keybuttonapplication

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent


class AccessibilityKeyDetector : AccessibilityService() {

    private val TAG = "AccessKeyDetector"

    override fun onKeyEvent(event: KeyEvent): Boolean {
        Log.d(TAG, "Key pressed via accessibility is: " + event.getKeyCode())
        //This allows the key pressed to function normally after it has been used by your app.
        if (event.keyCode == 24 && event.action == KeyEvent.ACTION_DOWN){
            if ((applicationContext as App).isTargetActivityStarted) {
                // Активити запущена и находится на переднем плане
            } else {
                val intent = Intent(this, AlarmActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
        return super.onKeyEvent(event)
    }



    override fun onServiceConnected() {
        Log.i(TAG, "Service connected")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}


    override fun onInterrupt() {}
}