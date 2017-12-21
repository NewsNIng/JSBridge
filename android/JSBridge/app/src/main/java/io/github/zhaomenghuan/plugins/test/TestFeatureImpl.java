package io.github.zhaomenghuan.plugins.test;

import android.util.Log;
import android.webkit.WebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.github.zhaomenghuan.jsbridge.CallbackContext;
import io.github.zhaomenghuan.jsbridge.JSBridgePlugin;

public class TestFeatureImpl extends JSBridgePlugin {
    private static String TAG = "TestFeatureImpl";

    public String syncTest(WebView webView, JSONArray array, CallbackContext callbackContext) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", "syncTest");
            jsonObject.put("function", "sync function");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    public void asyncTest(WebView webView, JSONArray array, CallbackContext callbackContext) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", "asyncTest");
            jsonObject.put("function", "async function");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        callbackContext.success(jsonObject);
    }

    public void onResume(boolean multitasking) {
        Log.e(TAG, "resume");
    }
}