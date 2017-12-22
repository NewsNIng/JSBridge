package io.github.zhaomenghuan.jsbridge;

import android.util.Log;
import android.webkit.WebView;

import org.json.JSONArray;
import org.json.JSONObject;

import io.github.zhaomenghuan.utils.JSUtil;

public class CallbackContext {
    private static final String TAG = "CallbackContext";

    private String callbackId;
    private WebView webView;
    protected boolean finished;
    private int changingThreads;

    public CallbackContext(String callbackId, WebView webView) {
        this.callbackId = callbackId;
        this.webView = webView;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isChangingThreads() {
        return changingThreads > 0;
    }

    public String getCallbackId() {
        return callbackId;
    }

    public String execSync(String value) {
        return JSUtil.wrapJsVar(value, true);
    }

    public String execSync(int value) {
        return JSUtil.wrapJsVar(Integer.toString(value), false);
    }

    public String execSync(JSONArray value) {
        return JSUtil.wrapJsVar(value.toString(), false);
    }

    public String execSync(JSONObject value) {
        return JSUtil.wrapJsVar(value.toString(), false);
    }

    /**
     * Helper for success callbacks that just returns the Status.OK by default
     *
     * @param message           The message to add to the success result.
     */
    public void success(JSONObject message) {
        sendPluginResult(new PluginResult(PluginResult.Status.OK, message));
    }

    /**
     * Helper for success callbacks that just returns the Status.OK by default
     *
     * @param message           The message to add to the success result.
     */
    public void success(String message) {
        sendPluginResult(new PluginResult(PluginResult.Status.OK, message));
    }

    /**
     * Helper for success callbacks that just returns the Status.OK by default
     *
     * @param message           The message to add to the success result.
     */
    public void success(JSONArray message) {
        sendPluginResult(new PluginResult(PluginResult.Status.OK, message));
    }

    /**
     * Helper for success callbacks that just returns the Status.OK by default
     *
     * @param message           The message to add to the success result.
     */
    public void success(byte[] message) {
        sendPluginResult(new PluginResult(PluginResult.Status.OK, message));
    }

    /**
     * Helper for success callbacks that just returns the Status.OK by default
     *
     * @param message           The message to add to the success result.
     */
    public void success(int message) {
        sendPluginResult(new PluginResult(PluginResult.Status.OK, message));
    }

    /**
     * Helper for success callbacks that just returns the Status.OK by default
     */
    public void success() {
        sendPluginResult(new PluginResult(PluginResult.Status.OK));
    }

    /**
     * Helper for error callbacks that just returns the Status.ERROR by default
     *
     * @param message           The message to add to the error result.
     */
    public void error(JSONObject message) {
        sendPluginResult(new PluginResult(PluginResult.Status.ERROR, message));
    }

    /**
     * Helper for error callbacks that just returns the Status.ERROR by default
     *
     * @param message           The message to add to the error result.
     */
    public void error(String message) {
        sendPluginResult(new PluginResult(PluginResult.Status.ERROR, message));
    }

    /**
     * Helper for error callbacks that just returns the Status.ERROR by default
     *
     * @param message           The message to add to the error result.
     */
    public void error(int message) {
        sendPluginResult(new PluginResult(PluginResult.Status.ERROR, message));
    }

    public void sendPluginResult(PluginResult pluginResult) {
        synchronized (this) {
            if (finished) {
                Log.w(TAG, "Attempted to send a second callback for ID: " + callbackId + "\nResult was: " + pluginResult.getMessage());
                return;
            } else {
                finished = !pluginResult.getKeepCallback();
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("JSBridge.callbackFromNative('")
                .append(callbackId)
                .append("',")
                .append(pluginResult.getStatus())
                .append(",[")
                .append(pluginResult.getMessage())
                .append("],")
                .append(pluginResult.getKeepCallback())
                .append(");");

        webView.loadUrl("javascript:" + sb);
    }
}
