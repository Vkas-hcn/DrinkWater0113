package com.snow.fall.willows.swaying.febfive.utils

import android.content.Context
import android.content.SharedPreferences
import com.snow.fall.willows.swaying.febfive.net.CanPost
import com.snow.fall.willows.swaying.febfive.start.FebApp
import java.text.SimpleDateFormat
import java.util.*

class H5Limiter {
    companion object {
        private const val PREF_LAST_HOUR = "last_hour_h5"
        private const val PREF_HOUR_COUNT = "hour_count_h5"
        private const val PREF_LAST_SHOW_DATE = "last_show_date_h5"
        private const val PREF_DAILY_SHOW_COUNT = "daily_show_count_h5"

        private var MAX_HOURLY_SHOWS = 0
        private var MAX_DAILY_SHOWS = 0
    }

    private fun maxHourlyShows() {
        val jsonBean = KeyContent.getAdminData() ?: return
        val dataString =
            jsonBean.wwwCan.split("-") ?: listOf("0", "0", "0", "0", "1", "1", "1")
        MAX_DAILY_SHOWS = dataString[0].toInt()
        MAX_HOURLY_SHOWS = dataString[1].toInt()
        KeyContent.showLog("h5-MAX_HOURLY_SHOWS=$MAX_HOURLY_SHOWS ----MAX_DAILY_SHOWS=${MAX_DAILY_SHOWS}")
    }

    // 检查是否可以展示广告
    fun canShowAd(): Boolean {
        maxHourlyShows()
        val prefs = getSharedPrefs()
        // 检查每日展示限制
        if (!checkDailyShowLimit(prefs)) {
            CanPost.getLiMitData()
            return false
        }
        // 检查小时限制
        if (!checkHourLimit(prefs)) {
            return false
        }
        return true
    }

    // 记录广告展示
    fun recordAdShown() {
        KeyContent.showLog("记录H5广告展示")
        val prefs = getSharedPrefs()
        val editor = prefs.edit()

        // 更新小时计数
        updateHourCount(prefs, editor)

        // 更新每日展示计数
        updateDailyShowCount(prefs, editor)

        editor.apply()
    }



    private fun getSharedPrefs() =
        FebApp.febApp.getSharedPreferences("ad_limits", Context.MODE_PRIVATE)

    private fun checkHourLimit(prefs: SharedPreferences): Boolean {
        val currentHour = getCurrentHourString()
        val lastHour = prefs.getString(PREF_LAST_HOUR, "")
        val hourCount = prefs.getInt(PREF_HOUR_COUNT, 0)

        // 如果进入新小时段则重置计数
        if (currentHour != lastHour) {
            prefs.edit()
                .putString(PREF_LAST_HOUR, currentHour)
                .putInt(PREF_HOUR_COUNT, 0)
                .apply()
            return true
        }
        KeyContent.showLog("h5-hourCount=$hourCount ----MAX_HOURLY_SHOWS=${MAX_HOURLY_SHOWS}")
        return hourCount < MAX_HOURLY_SHOWS
    }

    private fun checkDailyShowLimit(prefs: SharedPreferences): Boolean {
        val currentDate = getCurrentDateString()
        val lastDate = prefs.getString(PREF_LAST_SHOW_DATE, "")
        val dailyCount = prefs.getInt(PREF_DAILY_SHOW_COUNT, 0)

        // 如果进入新日期则重置计数
        if (currentDate != lastDate) {
            prefs.edit()
                .putString(PREF_LAST_SHOW_DATE, currentDate)
                .putInt(PREF_DAILY_SHOW_COUNT, 0)
                .apply()
            return true
        }
        KeyContent.showLog("h5-dailyCount=$dailyCount ----MAX_DAILY_SHOWS=${MAX_DAILY_SHOWS}")

        return dailyCount < MAX_DAILY_SHOWS
    }


    private fun updateHourCount(prefs: SharedPreferences, editor: SharedPreferences.Editor) {
        val currentHour = getCurrentHourString()
        val lastHour = prefs.getString(PREF_LAST_HOUR, "")

        if (currentHour == lastHour) {
            val newCount = prefs.getInt(PREF_HOUR_COUNT, 0) + 1
            editor.putInt(PREF_HOUR_COUNT, newCount)
        } else {
            editor.putString(PREF_LAST_HOUR, currentHour)
                .putInt(PREF_HOUR_COUNT, 1)
        }
    }

    private fun updateDailyShowCount(prefs: SharedPreferences, editor: SharedPreferences.Editor) {
        val currentDate = getCurrentDateString()
        val lastDate = prefs.getString(PREF_LAST_SHOW_DATE, "")

        if (currentDate == lastDate) {
            val newCount = prefs.getInt(PREF_DAILY_SHOW_COUNT, 0) + 1
            editor.putInt(PREF_DAILY_SHOW_COUNT, newCount)
        } else {
            editor.putString(PREF_LAST_SHOW_DATE, currentDate)
                .putInt(PREF_DAILY_SHOW_COUNT, 1)
        }
    }

    private fun getCurrentHourString() =
        SimpleDateFormat("yyyyMMddHH", Locale.getDefault()).format(Date())

    private fun getCurrentDateString() =
        SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
}