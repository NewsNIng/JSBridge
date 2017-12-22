package io.github.zhaomenghuan.utils;

import android.os.Build;
import android.webkit.WebView;

public class JSUtil {
    public static String wrapJsVar(String value, boolean isString) {
        StringBuilder sb = new StringBuilder("(function(){return ");
        if(isString) {
            sb.append("\'").append(value).append("\';");
        } else {
            sb.append(value).append(";");
        }
        sb.append("})()");
        return sb.toString();
    }

    public static void evaluateJavascript(WebView webView, String js) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript(js, null);
        } else {
            webView.loadUrl("javascript:" + js);
        }
    }
}