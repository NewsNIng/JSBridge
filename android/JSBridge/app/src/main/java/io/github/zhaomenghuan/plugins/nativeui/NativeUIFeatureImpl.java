package io.github.zhaomenghuan.plugins.nativeui;

import android.webkit.WebView;
import android.widget.Toast;

import org.json.JSONArray;

import io.github.zhaomenghuan.jsbridge.CallbackContext;
import io.github.zhaomenghuan.jsbridge.JSBridgePlugin;

public class NativeUIFeatureImpl extends JSBridgePlugin {
    private static final String TAG = "NativeUIFeatureImpl";

    public void showToast(WebView webView, JSONArray array, CallbackContext callbackContext) {
        String message = array.optString(0);
        String length = array.optString(1);
        int type = length.equals("L") ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        Toast.makeText(webView.getContext(), message, type).show();
    }
}