package com.wateww.jawaposdownloader;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DownloadActivity extends AppCompatActivity {
    private static final String LOGIN_URL = "http://digital.jawapos.com/index.php";
    private static final String PAPER_URL = "http://digital.jawapos.com/digital.php";
    private static final String LOGOUT_URL = "http://digital.jawapos.com/logout.php";
    private static final String DOWNLOAD_URL_FORMAT
            = "http://digital.jawapos.com/pdf.php?epaper=jawapos&date=%s";
    private static final String AUTO_LOGIN_SCRIPT =
            "javascript: {document.getElementsByName('usr')[0].value = 'sssandyad';" +
                    "document.getElementsByName('pas')[0].value = 'qwertyui';" +
                    "document.getElementsByName('btn_login')[0].click();};";

    private boolean startDownload = false;
    private boolean finishDownload = false;
    private WebView mWebView;

    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (mWebView != null) {
                finishDownload = true;
                mWebView.loadUrl(LOGOUT_URL);
            }
        }
    };

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWebView = new WebView(this);
        setContentView(mWebView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        CookieManager.getInstance().setAcceptCookie(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (finishDownload) {
                    finish();
                }

                if (!startDownload) {
                    if (url.equals(LOGIN_URL)) {
                        Log.d("COBA", "login");
                        view.loadUrl(AUTO_LOGIN_SCRIPT);
                    } else if (url.equals(PAPER_URL)) {
                        Log.d("COBA", "download");
                        startDownload = true;
                        downloadPdf();
                    }
                }
            }
        });
        mWebView.loadUrl("http://digital.jawapos.com/index.php");
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void downloadPdf() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String currentDateString = format.format(new Date());
        String downloadUrl = String.format(DOWNLOAD_URL_FORMAT, currentDateString);
        String cookie = CookieManager.getInstance().getCookie(downloadUrl);
        DownloadManager downloadmanager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(downloadUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(
                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.addRequestHeader("Cookie", cookie);
        downloadmanager.enqueue(request);
    }
}
