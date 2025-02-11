package com.snow.fall.willows.swaying.febfive.utils

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.os.PowerManager
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.snow.fall.willows.swaying.febfive.must.FacyData
import com.snow.fall.willows.swaying.febfive.net.CanPost
import com.snow.fall.willows.swaying.febfive.net.FebGetAllFun
import com.snow.fall.willows.swaying.febfive.start.FebApp.febApp
import com.snow.fall.willows.swaying.febfive.start.FebApp.isRelease
import com.snow.fall.willows.swaying.febfive.start.FebFive
import com.snow.fall.willows.swaying.febfive.utils.KeyContent.KEY_IS_ANDROID
import com.snow.fall.willows.swaying.febfive.utils.KeyContent.KEY_IS_FCM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

object AdUtils {
    var adShowTime: Long = 0
    var showAdTime: Long = 0
    fun sessionUp() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                CanPost.postPointDataWithHandler(false, "session_up")
                delay(1000 * 60 * 15)
            }
        }
    }

    fun noShowICCC() {
        CoroutineScope(Dispatchers.Main).launch {
            val isaData = KeyContent.getAdminData()
            if (isaData == null || !isaData.canNext) {
                KeyContent.showLog("不是A方案显示图标")
                FebFive.febSo("2xN@8tL#4wdm9qP6yM!3n", 2008f)  // TODO
            }
        }
    }

    fun initAppsFlyer() {
        KeyContent.showLog("AppsFlyer-id: $${FacyData.getAppsflyId()}")
        AppsFlyerLib.getInstance()
            .init(FacyData.getAppsflyId(), object : AppsFlyerConversionListener {
                override fun onConversionDataSuccess(conversionDataMap: MutableMap<String, Any>?) {
                    //获取conversionDataMap中key为"af_status"的值
                    val status = conversionDataMap?.get("af_status") as String?
                    KeyContent.showLog("AppsFlyer: $status")
                    CanPost.pointInstallAf(status.toString())
                    //打印conversionDataMap值
                    conversionDataMap?.forEach { (key, value) ->
                        KeyContent.showLog("AppsFlyer-all: key=$key: value=$value")
                    }
                }

                override fun onConversionDataFail(p0: String?) {
                    KeyContent.showLog("AppsFlyer: onConversionDataFail$p0")
                }

                override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
                    KeyContent.showLog("AppsFlyer: onAppOpenAttribution$p0")
                }

                override fun onAttributionFailure(p0: String?) {
                    KeyContent.showLog("AppsFlyer: onAttributionFailure$p0")
                }

            }, febApp)
        val adminData = SPUtils.getInstance(febApp).get(KEY_IS_ANDROID, "")

        AppsFlyerLib.getInstance().setCustomerUserId(adminData)
        AppsFlyerLib.getInstance().start(febApp)
        AppsFlyerLib.getInstance().logEvent(febApp, "jsonsshow", hashMapOf<String, Any>().apply {
            put("customer_user_id", adminData)
            put("app_version", FebGetAllFun.showAppVersion())
            put("os_version", Build.VERSION.RELEASE)
            put("bundle_id", febApp.packageName)
            put("language", "asc_wds")
            put("platform", "raincoat")
            put("android_id", adminData)
        })
    }

    fun initFaceBook() {
        val jsonBean = KeyContent.getAdminData()
        val data = jsonBean?.canInform?.split("-") ?: listOf("", "")

        if (data[1].isBlank()) {
            return
        }
        KeyContent.showLog("initFaceBook: ${data[1]}")
        FacebookSdk.setApplicationId(data[0])
        FacebookSdk.sdkInitialize(febApp)
        AppEventsLogger.activateApp(febApp)
    }


    fun getFcmFun() {
        if (!isRelease) return
        val localStorage = SPUtils.getInstance(febApp).get(KEY_IS_FCM, false)
        if (localStorage) return
        runCatching {
            Firebase.messaging.subscribeToTopic(FacyData.FCM)
                .addOnSuccessListener {
                    SPUtils.getInstance(febApp).put(KEY_IS_FCM, true)
                    KeyContent.showLog("Firebase: subscribe success")
                }
                .addOnFailureListener {
                    KeyContent.showLog("Firebase: subscribe fail")
                }
        }
    }

    fun canShowLocked(): Boolean {
        val powerManager = febApp.getSystemService(Context.POWER_SERVICE) as? PowerManager
        val keyguardManager = febApp.getSystemService(Context.KEYGUARD_SERVICE) as? KeyguardManager
        if (powerManager == null || keyguardManager == null) {
            return false
        }
        val isScreenOn = powerManager.isInteractive
        val isInKeyguardRestrictedInputMode = keyguardManager.inKeyguardRestrictedInputMode()

        return !isScreenOn || isInKeyguardRestrictedInputMode
    }


     fun adNumAndPoint(): Boolean {
        val adNum = SPUtils.getInstance(febApp).get(KeyContent.KEY_IS_AD_FAIL_COUNT, 0)
        val adminBean = KeyContent.getAdminData()

        if (adminBean == null) {
            KeyContent.showLog("AdminBean is null, cannot determine adNumAndPoint")
            return false
        }

        // 从配置中获取最大失败次数
        val maxFailNum = adminBean.timeCanNext?.split("-")?.getOrNull(3)?.toIntOrNull() ?: 0

        // 如果失败次数超过最大限制且需要重置
        if (adNum > maxFailNum && isDifferentDay(System.currentTimeMillis())) {
            resetFailureCount() // 重置失败次数
            return true
        }

        return false
    }

    private fun isDifferentDay(currentTime: Long): Boolean {
        val lastReportTime = SPUtils.getInstance(febApp).get(KeyContent.KEY_IS_LAST_REPORT_TIME, 0L)
        return !isSameDay(lastReportTime, currentTime)
    }

    private fun isSameDay(time1: Long, time2: Long): Boolean {
        val calendar1 = Calendar.getInstance().apply { timeInMillis = time1 }
        val calendar2 = Calendar.getInstance().apply { timeInMillis = time2 }

        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
                calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)
    }

    private fun resetFailureCount() {
        // 重置失败次数并更新最后报告时间
        SPUtils.getInstance(febApp).put(KeyContent.KEY_IS_AD_FAIL_COUNT, 0)
        SPUtils.getInstance(febApp).put(KeyContent.KEY_IS_LAST_REPORT_TIME, System.currentTimeMillis())
        KeyContent.showLog("Ad failure count has been reset")
    }

}