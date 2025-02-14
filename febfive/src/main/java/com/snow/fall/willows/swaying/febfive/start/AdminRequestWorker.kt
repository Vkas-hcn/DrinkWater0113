package com.snow.fall.willows.swaying.febfive.start

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.snow.fall.willows.swaying.febfive.net.FebGetAllFun
import com.snow.fall.willows.swaying.febfive.utils.KeyContent

class AdminRequestWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        KeyContent.showLog("Admin request started")
        FebGetAllFun.postAdminData(callback = object : FebGetAllFun.CallbackMy {
            override fun onSuccess(response: String) {
                KeyContent.showLog("Admin request successful: $response")
            }

            override fun onFailure(error: String) {
                KeyContent.showLog("Admin request failed: $error")
            }
        })
        return Result.success()
    }
}
