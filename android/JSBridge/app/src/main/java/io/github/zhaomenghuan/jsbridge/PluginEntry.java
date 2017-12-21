package io.github.zhaomenghuan.jsbridge;

/**
 * This class represents a service entry object.
 */
public final class PluginEntry {

    /**
     * The name of the service that this plugin implements
     */
    public final String service;

    /**
     * The plugin class name that implements the service.
     */
    public final String pluginClass;

    /**
     * The pre-instantiated plugin to use for this entry.
     */
    public final JSBridgePlugin plugin;

    /**
     * Flag that indicates the plugin object should be created when PluginManager is initialized.
     */
    public final boolean onload;

    /**
     * Constructs with a CordovaPlugin already instantiated.
     */
    public PluginEntry(String service, JSBridgePlugin plugin) {
        this(service, plugin.getClass().getName(), true, plugin);
    }

    /**
     * @param service               The name of the service
     * @param pluginClass           The plugin class name
     * @param onload                Create plugin object when HTML page is loaded
     */
    public PluginEntry(String service, String pluginClass, boolean onload) {
        this(service, pluginClass, onload, null);
    }

    private PluginEntry(String service, String pluginClass, boolean onload, JSBridgePlugin plugin) {
        this.service = service;
        this.pluginClass = pluginClass;
        this.onload = onload;
        this.plugin = plugin;
    }
}
