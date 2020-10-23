package com.kurotkin.keybuttonapplication

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //checkAccessibilityPermission()
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//            check()
//        }

        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivityForResult(intent, 1234)
            }
            val intent = Intent(this, MyService::class.java)
            startService(intent)
        } else {
            val intent = Intent(this, MyService::class.java)
            startService(intent)
        }
//        val intent = Intent(this, MyService::class.java)
//        startService(intent)
    }

//    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
//        when (keyCode) {
//            KeyEvent.KEYCODE_VOLUME_DOWN -> {
//                Toast.makeText(this, "Нажата кнопка громкости", Toast.LENGTH_SHORT).show()
//                vibro()
//            }
//        }
//        return false
//    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun check(){
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        startActivityForResult(intent, 1)
    }

//    private fun checkAccessibilityPermission(): Boolean {
//        var accessEnabled = 0
//        try {
//            accessEnabled = Settings.Secure.getInt(
//                this.contentResolver,
//                Settings.Secure.ACCESSIBILITY_ENABLED
//            )
//        } catch (e: Settings.SettingNotFoundException) {
//            e.printStackTrace()
//        }
//        return if (accessEnabled == 0) {
//            /** if not construct intent to request permission  */
//            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            /** request permission via start activity for result  */
//            startActivity(intent)
//            false
//        } else {
//            true
//        }
//    }

    fun vibro(t: Long = 2000){
        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(t, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            v.vibrate(t)
        }
    }
}