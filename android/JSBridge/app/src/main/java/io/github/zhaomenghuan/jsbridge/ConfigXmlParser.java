package io.github.zhaomenghuan.jsbridge;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ConfigXmlParser {
    private static String TAG = "ConfigXmlParser";

    private boolean insideFeature = false;
    private String service = "", pluginClass = "", paramType = "";
    private boolean onload = false;
    private ArrayList<PluginEntry> pluginEntries = new ArrayList<PluginEntry>(20);

    public ArrayList<PluginEntry> getPluginEntries() {
        return pluginEntries;
    }

    public void parse(Context context) {
        // XML解析器
        XmlPullParser xml = Xml.newPullParser();
        // 文件保存在assets目录下，得到assetManager管理器
        AssetManager assetManager = context.getAssets();
        try {
            // 打开文件，得到输入流
            InputStream is = assetManager.open("data/plugin.xml");
            xml.setInput(is, "utf-8");
            int eventType = xml.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    handleStartTag(xml);
                } else if (eventType == XmlPullParser.END_TAG) {
                    handleEndTag(xml);
                }
                eventType = xml.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleStartTag(XmlPullParser xml) {
        String strNode = xml.getName();
        if (strNode.equals("feature")) {
            // Check for supported feature sets aka. plugins (Accelerometer, Geolocation, etc)
            // Set the bit for reading params
            insideFeature = true;
            service = xml.getAttributeValue(null, "name");
        } else if (insideFeature && strNode.equals("param")) {
            paramType = xml.getAttributeValue(null, "name");
            // check if it is using the older service param
            if (paramType.equals("service")) {
                service = xml.getAttributeValue(null, "value");
            } else if (paramType.equals("package") || paramType.equals("android-package")) {
                pluginClass = xml.getAttributeValue(null, "value");
            } else if (paramType.equals("onload")) {
                onload = "true".equals(xml.getAttributeValue(null, "value"));
            }
        }
    }

    private void handleEndTag(XmlPullParser xml) {
        String strNode = xml.getName();
        if (strNode.equals("feature")) {
            pluginEntries.add(new PluginEntry(service, pluginClass, onload));
            service = "";
            pluginClass = "";
            insideFeature = false;
            onload = false;
        }
    }
}
