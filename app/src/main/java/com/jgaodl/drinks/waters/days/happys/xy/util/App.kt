package com.jgaodl.drinks.waters.days.happys.xy.util

import android.app.Application
import com.tencent.mmkv.MMKV

class App: Application() {

    companion object{

        lateinit var app: App
    }
    override fun onCreate() {
        super.onCreate()
        app = this
        MMKV.initialize(this)
        Local.mmkv = MMKV.defaultMMKV()
    }

}