package com.kurotkin.keybuttonapplication

import android.app.Activity
import android.app.Application
import android.os.Bundle


class App : Application(),  Application.ActivityLifecycleCallbacks{
    private var activityCount = 0
    var isTargetActivityStarted = false

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
    }

    fun isAppForeground(): Boolean {
        return activityCount > 0
    }

    override fun onActivityStopped(activity: Activity?) {}

    override fun onActivityStarted(activity: Activity?) {}

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}

    override fun onActivityResumed(activity: Activity?) {
        if (activity is AlarmActivity) {
            isTargetActivityStarted = true
        }
        activityCount++
    }

    override fun onActivityPaused(activity: Activity?) {
        if (activity is AlarmActivity) {
            isTargetActivityStarted = false
        }
        activityCount--
    }

    override fun onActivityDestroyed(activity: Activity?) {}

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {}
}