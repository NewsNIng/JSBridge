// toast
function showToastByJSBridge() {
    JSBridge.execSync("NativeUI", "showToast", ["hello world", "L"]);
}

// 同步测试
function syncTest() {
    var message = JSBridge.execSync("Test", "syncTest", ["hello world"]);
    alert(message.name);
}

// 异步测试
function asyncTest() {
    JSBridge.exec(function (message) {
        alert(message.name);
    }, null, "Test", "asyncTest", ["hello world"]);
}