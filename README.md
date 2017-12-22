# JSBridge

A communication bridge for Android and iOS.


## Usage

### JS 部分

#### 同步调用

``` js
Object JSBridge.execSync(service, action, args);
```

#### 异步调用

``` js
void JSBridge.exec(successCallback, errorCallback, service, action, args);
```

### 原生层

#### 同步调用

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

#### 异步调用

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