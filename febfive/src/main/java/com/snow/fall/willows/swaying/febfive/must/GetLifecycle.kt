package com.snow.fall.willows.swaying.febfive.must


import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.snow.fall.willows.swaying.febfive.SoCanActivity
import com.snow.fall.willows.swaying.febfive.must.ShowService.KEY_IS_SERVICE
import com.snow.fall.willows.swaying.febfive.net.CanPost
import com.snow.fall.willows.swaying.febfive.pz.so.FebFiveFffService
import com.snow.fall.willows.swaying.febfive.start.FebApp
import com.snow.fall.willows.swaying.febfive.start.FebApp.febApp
import com.snow.fall.willows.swaying.febfive.utils.KeyContent
import com.snow.fall.willows.swaying.febfive.utils.SPUtils


class GetLifecycle : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        ShowService.addActivity(activity)
        KeyContent.showLog("FebFiveFffService-launchQTServiceData---4-----${KEY_IS_SERVICE}")
        if (!KEY_IS_SERVICE) {
            KeyContent.showLog("FebFiveFffService-launchQTServiceData---5-----${KEY_IS_SERVICE}")
            ContextCompat.startForegroundService(
                febApp,
                Intent( febApp, FebFiveFffService::class.java)
            )
        }
    }

    override fun onActivityStarted(activity: Activity) {
        if (activity is SoCanActivity) {
            return
        }
        //TODO
        if (activity.javaClass.name.contains("com.walking.flames.under.street.ffsd.a.StartAAA")) {
            KeyContent.showLog("onActivityStarted-name=${activity.javaClass.name}")
            val anTime = ShowService.getInstallTimeDataFun()
            CanPost.postPointDataWithHandler(false, "session_front", "time", anTime)
        }
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        ShowService.removeActivity(activity)
    }

    override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
    }
}
