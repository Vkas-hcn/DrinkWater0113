package com.snow.fall.willows.swaying.febfive.must

import android.annotation.SuppressLint
import androidx.annotation.Keep
import com.snow.fall.willows.swaying.febfive.start.FebApp
import org.json.JSONObject

@Keep
object FacyData {
    const val FCM = "dfreivnk"
    val tttid: String = if (!FebApp.isRelease) {
        "114FE8DB631B3389BDDDD15D81E45E39"
    } else {
        "257BF41F4F0937B8AEA7F31E9B200294"
    }

    val openid: String = if (!FebApp.isRelease) {
        "0A600053F2B2775FF79B1CD046A0098C"
    } else {
        "EA76154CEF52340DCF932ABA93A87E04"
    }

    var upUrl = if (!FebApp.isRelease) {
        "https://test-bounty.recorddrinkbup.com/eyelet/removal"
    } else {
        "https://bounty.recorddrinkbup.com/hahn/modem"
    }

    var ADMIN_URL = if (!FebApp.isRelease) {
        "https://bup.recorddrinkbup.com/apitest/ssdd/"
    } else {
        "https://bup.recorddrinkbup.com/api/ssdd/"
    }

    fun getAppsflyId(): String {
        return if (!FebApp.isRelease) {
            "5MiZBZBjzzChyhaowfLpyR"
        } else {
            "X6QFbEQpPG2qjuCSNMxNA3"
        }
    }

    const val local_admin_json2 = """
{
    "canNext": true,//true:A用户。false:B用户
    "upIsGo": true,//true:可以上传。false:不可以上传
    "timeCanNext": "10-60-10-100-3-10-5",//分别是定时检测时间，距离安装时间X秒后外弹广告，广告展示间隔时间，小时展示上线，天展示上线，天点击上线。用-隔开
    "canInform": "366C94B8A3DAC162BC34E2A27DE4F130-3616318175247400-febfan",//广告id,fb id下发,外弹文件路径不能修改。
    "canDelay": "2000-3000",//随机延迟，起始时间、结束时间
    "wwwTime":"10",//前N秒
    "wwwPPPa":"com",//需传包名给h5
    "wwwUUUl":"www.google.com-wwww.google.com",//体外H5广告链接配置,体内H5广告链接配置,用-隔开
    "wwwCan":"5-10",//体外H5广告上线，单日跳转次数，单小时跳转次数，,用-隔开
}
    """

    const val local_admin_json = """
{
    "canNext": true,
    "upIsGo": false,
    "timeCanNext": "10-20-30-100-5-10-5",
    "canInform": "366C94B8A3DAC162BC34E2A27DE4F130x-3616318175247400-febfan",
    "canDelay": "2000-3000",
    "wwwTime":"10",
    "wwwPPPa":"com",
    "wwwUUUl":"https://www.baidu.com-https://www.google.com",
    "wwwCan":"10-2"
}
    """
}