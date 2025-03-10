package com.snow.fall.willows.swaying.febfive.net


import android.annotation.SuppressLint
import com.google.gson.Gson

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import android.util.Base64
import com.snow.fall.willows.swaying.febfive.must.AllDataBean
import com.snow.fall.willows.swaying.febfive.must.FacyData
import com.snow.fall.willows.swaying.febfive.start.FebApp.febApp
import com.snow.fall.willows.swaying.febfive.utils.KeyContent
import com.snow.fall.willows.swaying.febfive.utils.SPUtils
import java.io.IOException
import java.util.concurrent.TimeUnit

object FebGetAllFun {

    interface CallbackMy {
        fun onSuccess(response: String)
        fun onFailure(error: String)
    }
    fun showAppVersion(): String {
        return febApp.packageManager.getPackageInfo(febApp.packageName, 0).versionName?:""
    }



    @SuppressLint("HardwareIds")
    fun adminData(): String {
        val keyIsAndroid = SPUtils.getInstance(febApp).get(KeyContent.KEY_IS_ANDROID, "")
        val keyIsRef = SPUtils.getInstance(febApp).get(KeyContent.KEY_IS_REF, "")
        return JSONObject().apply {
            put("SMsjJII", "com.recorddrink.bup")
            put("oMC", keyIsAndroid)
            put("qzqvcjl", keyIsRef)
//            put("qzqvcjl", "555")
            put("XfVfU", showAppVersion())
        }.toString()
    }

    val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()
    fun postAdminData(callback: CallbackMy) {
        KeyContent.showLog("postAdminData=${adminData()}")
        val jsonBodyString = JSONObject(adminData()).toString()
        val timestamp = System.currentTimeMillis().toString()
        val xorEncryptedString = jxData(jsonBodyString, timestamp)
        val base64EncodedString = Base64.encodeToString(
            xorEncryptedString.toByteArray(StandardCharsets.UTF_8),
            Base64.NO_WRAP
        )


        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = base64EncodedString.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(FacyData.getAdminUrl())
            .post(requestBody)
            .addHeader("timestamp", timestamp)
            .build()
        CanPost.postPointDataWithHandler(false, "reqadmin")
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                KeyContent.showLog("admin----Request failed: ${e.message}")

                callback.onFailure("Request failed: ${e.message}")
                CanPost.getadmin(false,"timeout")
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    callback.onFailure("Unexpected code $response")
                    CanPost.getadmin(false,response.code.toString())
                    return
                }
                try {
                    val timestampResponse = response.header("timestamp")
                        ?: throw IllegalArgumentException("Timestamp missing in headers")

                    val decodedBytes = Base64.decode(response.body?.string() ?: "", Base64.DEFAULT)
                    val decodedString = String(decodedBytes, Charsets.UTF_8)
                    val finalData = jxData(decodedString, timestampResponse)
                    val jsonResponse = JSONObject(finalData)
                    val jsonData = parseAdminRefData(jsonResponse.toString())
                    val adminBean = runCatching {
                        Gson().fromJson(jsonData, AllDataBean::class.java)
                    }.getOrNull()

                    if (adminBean == null) {
                        callback.onFailure("The data is not in the correct format")
                        CanPost.getadmin(false,null)

                    } else {
                        if (KeyContent.getAdminData()==null) {
                            KeyContent.putAdminData(jsonData)
                        } else if (adminBean.canNext) {
                            KeyContent.putAdminData(jsonData)
                        }
                        CanPost.getadmin(adminBean.canNext,response.code.toString())

                        callback.onSuccess(jsonData)
                    }
                } catch (e: Exception) {
                    callback.onFailure("Decryption failed: ${e.message}")
                }
            }
        })

    }

    private fun jxData(text: String, timestamp: String): String {
        val cycleKey = timestamp.toCharArray()
        val keyLength = cycleKey.size
        return text.mapIndexed { index, char ->
            char.toInt().xor(cycleKey[index % keyLength].toInt()).toChar()
        }.joinToString("")
    }

    private fun parseAdminRefData(jsonString: String): String {
        try {
            val confString = JSONObject(jsonString).getJSONObject("NIbVnJ").getString("conf")
            return confString
        } catch (e: Exception) {
            return ""
        }
    }

    fun postPutData(body: Any, callbackData: CallbackMy) {
        val jsonBodyString = JSONObject(body.toString()).toString()
        val requestBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            jsonBodyString
        )

        val request = Request.Builder()
            .url(FacyData.getUpUrl())
            .post(requestBody)
            .addHeader("Content-Type", "application/json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                KeyContent.showLog("admin-Error: ${e.message}")
                callbackData.onFailure( e.message ?: "Unknown error")
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        callbackData.onFailure( "Unexpected code $response")
                    } else {
                        val responseData = response.body?.string() ?: ""
                        callbackData.onSuccess( responseData)
                    }
                }
            }
        })
    }

}
