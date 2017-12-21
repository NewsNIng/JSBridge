package io.github.zhaomenghuan.jsbridge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Plugins must extend this class and override one of the execute methods.
 */
public class JSBridgePlugin extends Intent {
    public WebView webView;
    private String serviceName;

    /**
     * Called after plugin construction and fields have been initialized.
     * Prefer to use pluginInitialize instead since there is no value in
     * having parameters on the initialize() function.
     */
    public void initialize(Context context, WebView webView) {
    }

    /**
     * Called after plugin construction and fields have been initialized.
     */
    protected void pluginInitialize() {
    }

    /**
     * Returns the plugin's service name (what you'd use when calling pluginManger.getPlugin())
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * Executes the request.
     *
     * This method is called from the WebView thread. To do a non-trivial amount of work, use:
     *     cordova.getThreadPool().execute(runnable);
     *
     * To run on the UI thread, use:
     *     cordova.getActivity().runOnUiThread(runnable);
     *
     * @param action          The action to execute.
     * @param rawArgs         The exec() arguments in JSON form.
     * @param callbackContext The callback context used when calling back into JavaScript.
     * @return                Whether the action was valid.
     */
    public boolean execute(String action, String rawArgs, CallbackContext callbackContext) throws JSONException {
        JSONArray args = new JSONArray(rawArgs);
        return execute(action, args, callbackContext);
    }

    /**
     * Executes the request.
     *
     * This method is called from the WebView thread. To do a non-trivial amount of work, use:
     *     cordova.getThreadPool().execute(runnable);
     *
     * To run on the UI thread, use:
     *     cordova.getActivity().runOnUiThread(runnable);
     *
     * @param action          The action to execute.
     * @param args            The exec() arguments, wrapped with some Cordova helpers.
     * @param callbackContext The callback context used when calling back into JavaScript.
     * @return                Whether the action was valid.
     */
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        return false;
    }

    /**
     * Called when the system is about to start resuming a previous activity.
     *
     * @param multitasking		Flag indicating if multitasking is turned on for app
     */
    public void onPause(boolean multitasking) {
    }

    /**
     * Called when the activity will start interacting with the user.
     *
     * @param multitasking		Flag indicating if multitasking is turned on for app
     */
    public void onResume(boolean multitasking) {
    }

    /**
     * Called when the activity is becoming visible to the user.
     */
    public void onStart() {
    }

    /**
     * Called when the activity is no longer visible to the user.
     */
    public void onStop() {
    }

    /**
     * Called when the activity receives a new intent.
     */
    public void onNewIntent(Intent intent) {
    }

    /**
     * The final call you receive before your activity is destroyed.
     */
    public void onDestroy() {
    }

    /**
     * Called when the Activity is being destroyed (e.g. if a plugin calls out to an external
     * Activity and the OS kills the CordovaActivity in the background). The plugin should save its
     * state in this method only if it is awaiting the result of an external Activity and needs
     * to preserve some information so as to handle that result; onRestoreStateForActivityResult()
     * will only be called if the plugin is the recipient of an Activity result
     *
     * @return  Bundle containing the state of the plugin or null if state does not need to be saved
     */
    public Bundle onSaveInstanceState() {
        return null;
    }

    /**
     * Called when a plugin is the recipient of an Activity result after the CordovaActivity has
     * been destroyed. The Bundle will be the same as the one the plugin returned in
     * onSaveInstanceState()
     *
     * @param state             Bundle containing the state of the plugin
     * @param callbackContext   Replacement Context to return the plugin result to
     */
    public void onRestoreStateForActivityResult(Bundle state, CallbackContext callbackContext) {}

    /**
     * Called when an activity you launched exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     *
     * @param requestCode   The request code originally supplied to startActivityForResult(),
     *                      allowing you to identify who this result came from.
     * @param resultCode    The integer result code returned by the child activity through its setResult().
     * @param intent        An Intent, which can return result data to the caller (various data can be
     *                      attached to Intent "extras").
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    }
}
