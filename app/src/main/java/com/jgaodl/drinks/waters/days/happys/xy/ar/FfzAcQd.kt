package com.jgaodl.drinks.waters.days.happys.xy.ar

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.util.Log
import androidx.annotation.Keep

@Keep
class FfzAcQd: BroadcastReceiver() {
    private val handler = Handler(Looper.getMainLooper())
    private var debounceRunnable: Runnable? = null

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.hasExtra("In")) {
            val eIntent = intent.getParcelableExtra<Parcelable>("In") as Intent?
            Log.e("TAG", "onReceive: 0")
            if (eIntent != null) {
                Log.e("TAG", "onReceive: 1", )
                debounceRunnable?.let { handler.removeCallbacks(it) }
                debounceRunnable = Runnable {
                    try {
                        Log.e("TAG", "onReceive: 2")
                        context.startActivity(eIntent)
                    } catch (e: Exception) {
                        Log.e("TAG", "startActivity failed: ${e.message}")
                    }
                }
                handler.postDelayed(debounceRunnable!!, 1000)
            }
        }
    }
}