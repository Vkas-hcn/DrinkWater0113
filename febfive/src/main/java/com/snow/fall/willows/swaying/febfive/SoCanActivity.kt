package com.snow.fall.willows.swaying.febfive

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.snow.fall.willows.swaying.febfive.dddh.Akadd
import com.snow.fall.willows.swaying.febfive.must.ShowService
import com.snow.fall.willows.swaying.febfive.net.CanPost
import com.snow.fall.willows.swaying.febfive.start.FebApp.adShowFun
import com.snow.fall.willows.swaying.febfive.utils.AdUtils
import com.snow.fall.willows.swaying.febfive.utils.KeyContent
import com.snow.fall.willows.swaying.febfive.utils.SPUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class SoCanActivity : AppCompatActivity() {
    private var activityJob: kotlinx.coroutines.Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Akadd.AfjruDd(this)
        Log.e("TAG", "onCreate: SoCanActivity", )
        adNumRef()
        isAdOrH5()
    }

    override fun onDestroy() {
        (this.window.decorView as ViewGroup).removeAllViews()
        super.onDestroy()
    }

    private fun isAdOrH5() {
        if (ShowService.isH5State) {
            wtH5()
        } else {
            CanPost.firstExternalBombPoint()
            wtAd()
        }
    }

    private fun wtAd() {
        val deData = getRandomNumberBetween()
        CanPost.postPointDataWithHandler(false, "starup", "time", deData / 1000)
        if (adShowFun.mTPInterstitial != null && adShowFun.mTPInterstitial!!.isReady) {
            CanPost.postPointDataWithHandler(false, "isready")
            KeyContent.showLog("广告展示随机延迟时间: $deData")
            activityJob = lifecycleScope.launch {
                delay(deData)
                CanPost.postPointDataWithHandler(false, "delaytime", "time", deData / 1000)
                AdUtils.showAdTime = System.currentTimeMillis()
                AdUtils.adShowTime = System.currentTimeMillis()
                adShowFun.mTPInterstitial!!.showAd(this@SoCanActivity, "sceneId")
                showSuccessPoint30()
            }
        } else {
            finish()
        }
    }

    private fun wtH5() {
        val intent = Intent(this, NetActivity::class.java)
        startActivity(intent)
        CanPost.postPointDataWithHandler(false, "browserjump")
    }

    private fun showSuccessPoint30() {
        lifecycleScope.launch {
            delay(30000)
            if (AdUtils.showAdTime > 0) {
                CanPost.postPointDataWithHandler(false, "show", "t", "30")
                AdUtils.showAdTime = 0
            }
        }
    }

    private fun adNumRef() {
        SPUtils.getInstance(this).put(KeyContent.KEY_IS_AD_FAIL_COUNT, 0)
    }

    private fun getRandomNumberBetween(): Long {
        val jsonBean = KeyContent.getAdminData()
        val range = jsonBean?.canDelay?.split("-")
        if (range != null && range.size == 2) {
            val minValue = range[0].toLong()
            val maxValue = range[1].toLong()
            return Random.nextLong(minValue, maxValue + 1)
        }
        return Random.nextLong(2000, 3000 + 1)
    }
}