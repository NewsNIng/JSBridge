(function(window) {
    var JSBRIDGE_PROTOCOL = 'JSBridge:';

    var utils = {
        typeName: function(val) {
            return Object.prototype.toString.call(val).slice(8, -1);
        }
    }

    window.JSBridge = {
        callbackId: Math.floor(Math.random() * 2000000000),
        callbacks: {},
        callbackStatus: {
            NO_RESULT: 0,
            OK: 1,
            CLASS_NOT_FOUND_EXCEPTION: 2,
            ILLEGAL_ACCESS_EXCEPTION: 3,
            INSTANTIATION_EXCEPTION: 4,
            MALFORMED_URL_EXCEPTION: 5,
            IO_EXCEPTION: 6,
            INVALID_ACTION: 7,
            JSON_EXCEPTION: 8,
            ERROR: 9
        },
        fireDocumentEvent: function(name, data) {
            var event = document.createEvent('Event');
            event.initEvent(name, true, true);
            event.arguments = data;
            document.dispatchEvent(event);
        },
        callbackFromNative: function(callbackId, status, args, keepCallback) {
            var callback = JSBridge.callbacks[callbackId];
            if (callback) {
                if (status == JSBridge.callbackStatus.OK) {
                    callback.success && callback.success.apply(null, args);
                } else {
                    callback.fail && callback.fail.apply(null, args);
                }
                // Clear callback if not expecting any more results
                if (!keepCallback) {
                    delete JSBridge.callbacks[callbackId];
                }
            }
        },
        exec: function(success, fail, service, action, args) {
            // If args is not provided, default to an empty array
            args = args || [];

            // Process any ArrayBuffers in the args into a string.
            for (var i = 0; i < args.length; i++) {
                if (utils.typeName(args[i]) == 'ArrayBuffer') {
                    args[i] = base64.fromArrayBuffer(args[i]);
                }
            }

            var callbackId = service + JSBridge.callbackId++,
                argsJson = JSON.stringify(args);
            if (success || fail) {
                JSBridge.callbacks[callbackId] = {success:success, fail:fail};
            }

            return prompt(args, JSBRIDGE_PROTOCOL + JSON.stringify([service, action, callbackId]))
        }
    }


})(window);

// toast
function showToastByJSBridge() {
    JSBridge.exec(null, null, "NativeUI", "showToast", ["hello world", "L"]);
}

// 同步测试
function syncTest() {
    var str = JSBridge.exec(null, null, "Test", "syncTest", ["hello world"]);
    alert(str);
}

// 异步测试
function asyncTest() {
    JSBridge.exec(function(str) {
        alert(JSON.stringify(str));
    }, null, "Test", "asyncTest", ["hello world"]);
}