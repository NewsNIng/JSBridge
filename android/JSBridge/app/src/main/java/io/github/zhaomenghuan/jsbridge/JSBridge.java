package io.github.zhaomenghuan.jsbridge;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

import io.github.zhaomenghuan.jsbridge.engine.JSBridgeWebChromeClient;

public class JSBridge {
    private static String TAG = "JSBridge";

    Context mContext;
    WebView mWebView;
    Collection<PluginEntry> pluginEntries;
    LinkedHashMap<String, PluginEntry> entryMap = new LinkedHashMap<String, PluginEntry>();
    LinkedHashMap<String, JSBridgePlugin> pluginMap = new LinkedHashMap<String, JSBridgePlugin>();

    public JSBridge(Context context, WebView webView) {
        mContext = context;
        mWebView = webView;

        init();
    }

    public void init() {
        // 加载配置文件
        loadConfig();

        // 设置插件列表
        setPluginEntries();

        // 开启debug
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.setWebContentsDebuggingEnabled(true);
        }

        // 设置 webview Settings
        setWebSettings();
        // 设置 WebViewClient
        mWebView.setWebViewClient(new WebViewClient());
        // 设置 WebChromeClient
        mWebView.setWebChromeClient(new JSBridgeWebChromeClient(this));
    }

    private void loadConfig() {
        ConfigXmlParser parser = new ConfigXmlParser();
        parser.parse(mContext);
        pluginEntries = parser.getPluginEntries();
    }

    private void setPluginEntries() {
        for (PluginEntry entry : pluginEntries) {
            this.entryMap.put(entry.service, entry);
            // Log.e(TAG, "PluginEntry => service: " + entry.service + " pluginClass: " + entry.pluginClass + " plugin: " + entry.plugin);
            if (entry.onload) {
                getPlugin(entry.service);
            } else {
                pluginMap.put(entry.service, null);
            }
        }
    }

    private void setWebSettings() {
        WebSettings webSettings = mWebView.getSettings();
        // 开启JavaScript代码
        webSettings.setJavaScriptEnabled(true);
    }

    /**
     * 加载资源
     */
    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    public JSBridgePlugin getPlugin(String service) {
        JSBridgePlugin ret = pluginMap.get(service);
        if (ret == null) {
            PluginEntry pe = entryMap.get(service);
            if (pe == null) {
                return null;
            }
            if (pe.plugin != null) {
                ret = pe.plugin;
            } else {
                ret = instantiatePlugin(pe.pluginClass);
            }
            pluginMap.put(service, ret);
        }
        return ret;
    }

    private JSBridgePlugin instantiatePlugin(String className) {
        JSBridgePlugin ret = null;
        try {
            if(!TextUtils.isEmpty(className)) {
                Class<?> cls = mContext.getClass().forName(className);
                if (cls != null) {
                    ret = (JSBridgePlugin) cls.newInstance();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error adding plugin " + className + ".");
        }
        return ret;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Object jsCallJava(WebView webView, String message, String defaultValue) {
        String JSBRIDGE_PROTOCOL = "JSBridge:";

        if (defaultValue != null && defaultValue.startsWith(JSBRIDGE_PROTOCOL)) {
            JSONArray array;
            try {
                array = new JSONArray(defaultValue.replace(JSBRIDGE_PROTOCOL, ""));
                String service = array.getString(0);
                String action = array.getString(1);
                String callbackId = array.getString(2);
                String[] params = message.split(",");

                JSBridgePlugin instance = getPlugin(service);
                Method[] methods = instance.getClass().getDeclaredMethods();
                for (Method method: methods) {
                    if(action.equals(method.getName())) {
                        Log.i(TAG, "service: " + service + ";action: " + action + ";callbackId: " + callbackId + ";param: " + message);
                        return method.invoke(instance, webView, new JSONArray(params), new CallbackContext(callbackId, webView));
                    }
                }
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Called when the activity will start interacting with the user.
     *
     * @param multitasking Flag indicating if multitasking is turned on for app
     */
    public void onResume(boolean multitasking) {
        for (JSBridgePlugin plugin : this.pluginMap.values()) {
            if (plugin != null) {
                plugin.onResume(multitasking);
            }
        }
    }

    /**
     * Called when the activity is becoming visible to the user.
     */
    public void onStart() {
        for (JSBridgePlugin plugin : this.pluginMap.values()) {
            if (plugin != null) {
                plugin.onStart();
            }
        }
    }

    /**
     * Called when the activity is no longer visible to the user.
     */
    public void onStop() {
        for (JSBridgePlugin plugin : this.pluginMap.values()) {
            if (plugin != null) {
                plugin.onStop();
            }
        }
    }

    /**
     * The final call you receive before your activity is destroyed.
     */
    public void onDestroy() {
        for (JSBridgePlugin plugin : this.pluginMap.values()) {
            if (plugin != null) {
                plugin.onDestroy();
            }
        }
        // We should use a blank data: url instead so it's more obvious
        mWebView.loadUrl("about:blank");
        mWebView.destroy();
    }

    /**
     * Called when the activity receives a new intent.
     */
    public void onNewIntent(Intent intent) {
        for (JSBridgePlugin plugin : this.pluginMap.values()) {
            if (plugin != null) {
                plugin.onNewIntent(intent);
            }
        }
    }
}
