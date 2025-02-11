package com.snow.fall.willows.swaying.febfive.dddh;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.snow.fall.willows.swaying.febfive.utils.KeyContent;

public class Ckdd extends WebViewClient {
	
    @Override
    public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        KeyContent.INSTANCE.showLog(" onPageStarted=url="+url);

    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        KeyContent.INSTANCE.showLog(" onPageFinished=url="+url);
    }
}
