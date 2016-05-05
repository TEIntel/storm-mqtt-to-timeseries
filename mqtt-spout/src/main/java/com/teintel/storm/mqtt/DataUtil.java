package com.teintel.storm.mqtt;

/**
 * Created by teveritt on March 06, 2016.
 */
public class DataUtil {

    public static String sanitise(String text) {
        text = text.toLowerCase();
        text = text.replaceAll("\\s+", "-");
        text = text.replaceAll("\\.", "_");
        return text;
    }
}
