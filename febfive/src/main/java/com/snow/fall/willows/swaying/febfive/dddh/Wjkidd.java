package com.snow.fall.willows.swaying.febfive.dddh;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.Keep;

import com.snow.fall.willows.swaying.febfive.utils.KeyContent;

@Keep
public class Wjkidd extends WebChromeClient {
    @Override
    public void onProgressChanged(WebView webView, int i10) {
        super.onProgressChanged(webView, i10);
        KeyContent.INSTANCE.showLog(" onPageStarted=url="+i10);

        if (i10 == 100) {
            Akadd.UjdrDd(i10);
        }
    }
}
