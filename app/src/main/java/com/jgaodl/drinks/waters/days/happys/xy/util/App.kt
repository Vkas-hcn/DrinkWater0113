package com.jgaodl.drinks.waters.days.happys.xy.util

import android.app.Application
//import com.snow.fall.willows.swaying.febfive.must.FacyData
//import com.snow.fall.willows.swaying.febfive.must.GetLifecycle
//import com.snow.fall.willows.swaying.febfive.start.FebApp
import com.tencent.mmkv.MMKV
//import com.tradplus.ads.open.TradPlusSdk

class App: Application() {

    companion object{

        lateinit var app: App
    }
    override fun onCreate() {
        super.onCreate()
        app = this
        MMKV.initialize(this)
        Local.mmkv = MMKV.defaultMMKV()
//        val lifecycleObserver = GetLifecycle()
//        registerActivityLifecycleCallbacks(lifecycleObserver)
//        FebApp.init(this, false)
    }
}