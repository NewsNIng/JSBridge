(function (window) {
    window.JSBridge = {
        JSBRIDGE_PROTOCOL: 'JSBridge:',
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
        fireDocumentEvent: function (name, data) {
            var event = document.createEvent('Event');
            event.initEvent(name, true, true);
            event.arguments = data;
            document.dispatchEvent(event);
        },
        callbackFromNative: function (callbackId, status, args, keepCallback) {
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
        exec: function (success, fail, service, action, args) {
            args = args || [];

            var callbackId = service + JSBridge.callbackId++,
                argsJson = JSON.stringify(args);
            if (success || fail) {
                JSBridge.callbacks[callbackId] = { success: success, fail: fail };
            }

            return prompt(args, JSBridge.JSBRIDGE_PROTOCOL + JSON.stringify([service, action, callbackId]))
        },
        execSync: function (service, action, args) {
            var ret = JSBridge.exec(null, null, service, action, args);
            return ret ? window.eval(ret) : null;
        }
    }

})(window);