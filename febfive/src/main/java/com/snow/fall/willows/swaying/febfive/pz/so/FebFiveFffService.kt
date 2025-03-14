package com.snow.fall.willows.swaying.febfive.pz.so

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import android.widget.RemoteViews
import com.snow.fall.willows.swaying.febfive.R
import com.snow.fall.willows.swaying.febfive.must.ShowService.KEY_IS_SERVICE
import com.snow.fall.willows.swaying.febfive.start.FebApp.febApp
import com.snow.fall.willows.swaying.febfive.utils.KeyContent
import com.snow.fall.willows.swaying.febfive.utils.SPUtils

class FebFiveFffService : Service() {
    @SuppressLint("ForegroundServiceType", "RemoteViewLayout")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        KeyContent.showLog("FebFiveFffService onStartCommand-1=${KEY_IS_SERVICE}")
        if (!KEY_IS_SERVICE) {
            KEY_IS_SERVICE =true
            val channel = NotificationChannel("febfivestate", "febfivestate", NotificationManager.IMPORTANCE_MIN)
            channel.setSound(null, null)
            channel.enableLights(false)
            channel.enableVibration(false)
            (application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).run {
                if (getNotificationChannel(channel.toString()) == null) createNotificationChannel(channel)
            }
            runCatching {
                startForeground(
                    3946,
                    NotificationCompat.Builder(this, "febfivestate").setSmallIcon(R.drawable.shape_no)
                        .setContentText("")
                        .setContentTitle("")
                        .setOngoing(true)
                        .setCustomContentView(RemoteViews(packageName, R.layout.layout_no))
                        .build()
                )
            }
            KeyContent.showLog("FebFiveFffService onStartCommand-2=${KEY_IS_SERVICE}")
        }
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


}
