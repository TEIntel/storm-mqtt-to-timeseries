package com.teintel.storm.mqtt.collectd;

import java.util.LinkedHashMap;
import java.util.Map;

import backtype.storm.mqtt.MqttMessage;
import com.teintel.storm.mqtt.DBMessage;
import com.teintel.storm.mqtt.DataUtil;
import com.teintel.storm.mqtt.TimeSeriesDBMetricBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Created by teveritt on March 06, 2016.
 */
public abstract class CollectdMqttMessage implements DBMessage {
    public CollectdMqttMessage(MqttMessage message) {
        // Topic Syntax: host "/" plugin ["-" plugin instance] "/" type ["-" type instance]
        String topic = message.getTopic();
        String[] topicElements = topic.split("/");
        String payload = new String(message.getMessage());
        String[] payloadElements = payload.split(":");

        host = topicElements[1];
        plugin = topicElements[2];
        pluginInstance = "";
        type = topicElements[3];
        typeInstance = "";
        String timeStr = StringUtils.trim(payloadElements[0]);

        time = NumberUtils.toLong(timeStr.replace(".", ""));


        String[] pluginSplit = plugin.split("-", 2);
        String[] typeSplit = type.split("-", 2);

        if (pluginSplit.length == 2) {
            plugin = pluginSplit[0];
            pluginInstance = pluginSplit[1];
        }


        if (typeSplit.length == 2) {
            type = typeSplit[0];
            typeInstance = typeSplit[1];
        }

        if (StringUtils.startsWith(type, plugin + "_")) {
            type = type.substring((plugin + "_").length());
        }

        host = DataUtil.sanitise(host);
        plugin = DataUtil.sanitise(plugin);
        type = DataUtil.sanitise(type);
        typeInstance = DataUtil.sanitise(typeInstance);
        pluginInstance = DataUtil.sanitise(pluginInstance);
        typeInstance = DataUtil.sanitise(typeInstance);


        valueMap = new LinkedHashMap<>();
        if (payloadElements.length > 2) {
            for (int i = 1; i < payloadElements.length; i++) {
                String type = payloadType(i - 1);
                String valStr = StringUtils.trim(payloadElements[i]);
                Double val = NumberUtils.isNumber(valStr) ? NumberUtils.toDouble(valStr) : 0.0;
                valueMap.put(type, val);
            }
        } else {
            String valStr = StringUtils.trim(payloadElements[1]);
            Double val = NumberUtils.isNumber(valStr) ? NumberUtils.toDouble(valStr) : 0.0;
            valueMap.put("default", val);
        }
    }

    public String getHost() {
        return host;
    }

    public String getPlugin() {
        return plugin;
    }

    public String getPluginInstance() {
        return pluginInstance;
    }

    @Override
    public String toString() {
        return "CollectdMqttMessage{" +
                "host='" + host + '\'' +
                ", plugin='" + plugin + '\'' +
                ", pluginInstance='" + pluginInstance + '\'' +
                ", type='" + type + '\'' +
                ", typeInstance='" + typeInstance + '\'' +
                ", time=" + time +
                ", valueMap=" + valueMap +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CollectdMqttMessage)) return false;

        CollectdMqttMessage that = (CollectdMqttMessage) o;

        if (host != null ? !host.equals(that.host) : that.host != null) return false;
        if (plugin != null ? !plugin.equals(that.plugin) : that.plugin != null) return false;
        if (pluginInstance != null ? !pluginInstance.equals(that.pluginInstance) : that.pluginInstance != null)
            return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (typeInstance != null ? !typeInstance.equals(that.typeInstance) : that.typeInstance != null) return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;
        return !(valueMap != null ? !valueMap.equals(that.valueMap) : that.valueMap != null);

    }

    @Override
    public int hashCode() {
        int result = host != null ? host.hashCode() : 0;
        result = 31 * result + (plugin != null ? plugin.hashCode() : 0);
        result = 31 * result + (pluginInstance != null ? pluginInstance.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (typeInstance != null ? typeInstance.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        result = 31 * result + (valueMap != null ? valueMap.hashCode() : 0);
        return result;
    }

    public String payloadType(int index) {

        if (StringUtils.equalsIgnoreCase(plugin, "interface")) {
            switch (index) {
                case 0:
                    return "rx";
                case 1:
                    return "tx";
            }
        } else if (StringUtils.equalsIgnoreCase(plugin, "disk")) {
            switch (index) {
                case 0:
                    return "read";
                case 1:
                    return "write";
            }
        }

        return "val_" + index;
    }

    public String getType() {
        return type;
    }

    public String getTypeInstance() {
        return typeInstance;
    }

    public Long getTime() {
        return time;
    }

    public Map<String, Double> getValues() {
        return valueMap;
    }

    @Override
    public abstract TimeSeriesDBMetricBuilder createDBMetricBuilder();

    private String host;

    private String plugin;

    private String pluginInstance;

    private String type;

    private String typeInstance;

    private Long time;

    private Map<String, Double> valueMap;


}
