// MainActivity.java
package com.putra.kioskbrowser;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceError;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private static final String URL = "http://192.168.10.250";
    private static final String TAG = "KioskBrowser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        Log.d(TAG, "onCreate: Initializing WebView");

        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(webView, true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(TAG, "Loading URL: " + url);
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Log.e(TAG, "Error loading URL: " + request.getUrl() + " Error: " + error.getDescription());
                super.onReceivedError(view, request, error);
                // Reload page automatically on error
                view.postDelayed(() -> view.loadUrl(URL), 2000);
            }
        });

        Log.d(TAG, "Loading initial URL: " + URL);
        webView.loadUrl(URL);

        // Start Lock Task Mode to enforce kiosk mode
        Log.d(TAG, "Starting Lock Task Mode");
        startLockTask();
    }

    @Override
    public void onBackPressed() {
        // Disable back button to prevent leaving kiosk mode
    }

    public void reloadPage(View view) {
        Log.d(TAG, "Reloading page");
        webView.reload();
    }

    public void exitApp(View view) {
        Log.d(TAG, "Exiting app");
        stopLockTask();  // Stop Kiosk Mode
        finish();
    }
}