package io.github.zhaomenghuan;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;

import io.github.zhaomenghuan.jsbridge.JSBridge;

public class MainActivity extends Activity {
    private static String TAG = "App";

    protected boolean keepRunning = true;

    private WebView webView;
    private Context context;
    private JSBridge jsBridge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        webView = new WebView(context);

        // 初始化 JSBridge
        jsBridge = new JSBridge(context, webView);
        jsBridge.loadUrl("file:///android_asset/www/index.html");

        setContentView(webView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        jsBridge.onResume(this.keepRunning);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Forward to plugins
        jsBridge.onDestroy();
    }
}
