package com.jgaodl.drinks.waters.days.happys.xy

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jgaodl.drinks.waters.days.happys.xy.databinding.ActivityMainBinding
import com.jgaodl.drinks.waters.days.happys.xy.util.Local
import com.tradplus.ads.base.bean.TPAdError
import com.tradplus.ads.base.bean.TPAdInfo
import com.tradplus.ads.base.bean.TPBaseAd
import com.tradplus.ads.open.splash.SplashAdListener
import com.tradplus.ads.open.splash.TPSplash
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ss = Local.getTotalGoals()
        if (ss.isEmpty()) {
            Local.setTotalGoals("2500")
        }
        initAd(this)
    }

    var job: Job? = null
    fun load() {
        job = lifecycleScope.launch(Dispatchers.IO) {
            withTimeoutOrNull(1200L) {
                delay(1500)
            }

            val isFirst = Local.getIsFirst()
            if (isFirst) {
                startActivity(Intent(this@MainActivity, HomeActivity::class.java))
            } else {
                startActivity(Intent(this@MainActivity, StartActivity::class.java))
            }

        }
    }

    override fun onResume() {
        super.onResume()
//        load()
    }

    override fun onPause() {
        job?.cancel()
        super.onPause()
    }

    // 广告对象
    private var mTPSplash: TPSplash? = null

    // 广告容器
    private var adContainer: FrameLayout? = null
    private var jobOpenTdo: Job? = null

    // 初始化广告位
    private fun initAd(context: Context) {
        if (mTPSplash == null) {
            mTPSplash = TPSplash(context, "0A600053F2B2775FF79B1CD046A0098C")
            mTPSplash!!.setAdListener(object : SplashAdListener() {
                override fun onAdLoaded(tpAdInfo: TPAdInfo, tpBaseAd: TPBaseAd?) {
                    Log.e("TAG", "开屏广告加载成功")
                    // 广告加载成功
                }

                override fun onAdLoadFailed(tpAdError: TPAdError) {
                    // 广告加载失败
                    Log.e("TAG", "开屏广告加载失败=${tpAdError.errorCode}--${tpAdError.errorMsg}")
                }

                override fun onAdClosed(tpAdInfo: TPAdInfo) {
                    Log.e("TAG", "开屏广告关闭")
                    adContainer?.removeAllViews()
                    // 进入应用主界面
                    navigateToMainActivity()
                }
            })
        }
        mTPSplash?.loadAd(null)
        showOpenAd()
    }


    private fun showOpenAd() {
        jobOpenTdo?.cancel()
        jobOpenTdo = null
        jobOpenTdo = lifecycleScope.launch {
            try {
                withTimeout(10000L) {
                    while (isActive) {
//                        Log.e("TAG", "showAd: ${mTPSplash?.isReady}")

                        if (mTPSplash?.isReady == true) {
                            mTPSplash?.showAd(binding.splashContainer)
                            break
                        }
                        delay(500L)
                    }
                }
            } catch (e: TimeoutCancellationException) {
                navigateToMainActivity()
            }
        }
    }

    // 判断应用是否在前台
    private fun isAppInForeground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses
        for (appProcess in appProcesses) {
            if (appProcess.processName == context.packageName) {
                return appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
            }
        }
        return false
    }

    // 进入应用主界面
    private fun navigateToMainActivity() {
        val isFirst = Local.getIsFirst()
        if (isFirst) {
            startActivity(Intent(this@MainActivity, HomeActivity::class.java))
        } else {
            startActivity(Intent(this@MainActivity, StartActivity::class.java))
        }
        finish()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (KeyEvent.KEYCODE_BACK == keyCode || KeyEvent.KEYCODE_HOME == keyCode) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}