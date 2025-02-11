package com.snow.fall.willows.swaying.febfive.pz.rock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Parcelable


class FebFiveMmmRecent: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.hasExtra("I")) {
            val eIntent = intent.getParcelableExtra<Parcelable>("I") as Intent?
            if (eIntent != null) {
                try {
                    context.startActivity(eIntent)
                    return
                } catch (e: Exception) {
                }
            }
        }
    }
}