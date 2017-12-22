# JSBridge

A communication bridge for JavaScript with Android or iOS.

## 初始化

Android 初始化：

``` java
JSBridge jsBridge = new JSBridge(context, webView);
jsBridge.loadUrl("file:///android_asset/www/index.html");
```

## 用法

### JS层调用Java层

#### JS 部分

##### 同步调用:

``` js
Object JSBridge.execSync(service, action, args);
```

##### 异步调用:

``` js
void JSBridge.exec(successCallback, errorCallback, service, action, args);
```

#### native 部分

##### 同步调用:

``` java
public String syncTest(WebView webView, JSONArray array, CallbackContext callbackContext) {
    JSONObject jsonObject = new JSONObject();
    try {
        jsonObject.put("name", "syncTest");
        jsonObject.put("function", "sync function");
    } catch (JSONException e) {
        e.printStackTrace();
    }

    return callbackContext.execSync(jsonObject);
}
```

##### 异步调用:

``` java
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
```

### Java层调用JS层

Android 原生提供了执行JavaScript的API，可以进一步封装：

``` java
public static void evaluateJavascript(WebView webView, String js) {
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        webView.evaluateJavascript(js, null);
    } else {
        webView.loadUrl("javascript:" + js);
    }
}
```

在原生层我们可以使用JSBridge实例进行调用：
``` java
JSBridge jsBridge = new JSBridge(context, webView);
jsBridge.evaluateJavascript(String js);
```

在插件里面可以通过 `JSUtil.evaluateJavascript(WebView webView, String js)`调用。

JS层如果想接受原生层主动发送的消息，需要通过事件监听的方式去执行，例如：

``` java
webView.setWebViewClient(new WebViewClient() {
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

        JSUtil.evaluateJavascript(view, "JSBridge.fireDocumentEvent('titleUpdate', '" + view.getTitle() + "')")
    }
});
```

JS层监听原生层的消息：
```
document.addEventListener("titleUpdate", function(msg) {
    alert("title: "+ msg.arguments);
});
```

## TODO

- [x] Android: JS 与 Java 通信机制
- [ ] Android: JS 反射调用原生API的机制
- [ ] Android: 原生UI引擎
- [ ] iOS: JS 与 OC 通信机制
- [ ] iOS: JS 反射调用原生API的机制
- [ ] iOS: 原生UI引擎
