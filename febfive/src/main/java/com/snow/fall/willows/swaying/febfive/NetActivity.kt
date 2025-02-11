package com.snow.fall.willows.swaying.febfive

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient // 添加导入语句
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.snow.fall.willows.swaying.febfive.net.CanPost
import com.snow.fall.willows.swaying.febfive.start.FebApp
import com.snow.fall.willows.swaying.febfive.utils.KeyContent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NetActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_net)
        val webView: WebView = findViewById(R.id.web_net)
        val webLoading: LinearLayout = findViewById(R.id.ll_loading)
        val webSettings: WebSettings = webView.settings
        click()
        FebApp.h5Limiter.recordAdShown()
        val beanData = KeyContent.getAdminData() ?: return
        val range = beanData.wwwUUUl.split("-")[0]
        KeyContent.showLog("back package:${beanData.wwwPPPa}")
        KeyContent.showLog("loadUrl:${range}")
        webSettings.userAgentString += "/${beanData.wwwPPPa}"
        webSettings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        lifecycleScope.launch {
            delay(3000)
            webLoading.visibility = LinearLayout.GONE
            webView.loadUrl(range)
        }
    }

    private fun click() {
        val ivClose: ImageView = findViewById(R.id.iv_close)
        ivClose.setOnClickListener {
            finish()
            CanPost.postPointDataWithHandler(false, "closebrowser")

        }
    }
}