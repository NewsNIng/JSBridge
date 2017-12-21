package io.github.zhaomenghuan.jsbridge.engine;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import io.github.zhaomenghuan.jsbridge.JSBridge;

public class JSBridgeWebChromeClient extends WebChromeClient {
    private static String TAG = "JSBridgeWebChromeClient";
    private JSBridge bridge;
    public JSBridgeWebChromeClient(JSBridge jsBridge) {
        bridge = jsBridge;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
        if (defaultValue != null && defaultValue.startsWith("JSBridge:")) {
            final Object resultObject = bridge.jsCallJava(view, message, defaultValue);
            String ret = resultObject != null ? resultObject.toString(): null;
            result.confirm(ret);
        }

        return true;
    }
}
