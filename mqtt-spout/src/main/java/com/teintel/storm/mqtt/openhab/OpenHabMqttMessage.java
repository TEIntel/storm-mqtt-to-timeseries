package com.teintel.storm.mqtt.openhab;

import backtype.storm.mqtt.MqttMessage;
import com.teintel.storm.mqtt.DBMessage;
import com.teintel.storm.mqtt.DataUtil;
import com.teintel.storm.mqtt.TimeSeriesDBMetricBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Created by teveritt on March 06, 2016.
 */
public abstract class OpenHabMqttMessage implements DBMessage {
    public OpenHabMqttMessage(MqttMessage message) {

        String topic = message.getTopic();
        String[] topicElements = topic.split("/");
        String payload = StringUtils.trim(new String(message.getMessage()));

        site = DataUtil.sanitise(topicElements[2]);
        String name = topicElements[3];
        eventType = DataUtil.sanitise(topicElements[4]);
        value = 0.0;

        itemName = "";
        String[] nameSplit = name.split("_");
        for (String n : nameSplit) {
            if (!StringUtils.isEmpty(itemName)) {
                if (NumberUtils.isNumber(n)) {
                    itemName += "_";
                } else {
                    itemName += ".";
                }
            }

            itemName += DataUtil.sanitise(n);
        }

        if (NumberUtils.isNumber(payload)) {
            value = NumberUtils.toDouble(payload);
        } else if (StringUtils.equalsIgnoreCase(payload, "ON")) {
            value = 1.0;
        } else if (StringUtils.equalsIgnoreCase(payload, "OFF")) {
            value = 0.0;
        } else if (StringUtils.equalsIgnoreCase(payload, "OPEN")) {
            value = 1.0;
        } else if (StringUtils.equalsIgnoreCase(payload, "CLOSED")) {
            value = 0.0;
        } else {
            value = null;
        }


    }

    @Override
    public String toString() {
        return "OpenHabMqttMessage{" +
                "site='" + site + '\'' +
                ", itemName='" + itemName + '\'' +
                ", eventType='" + eventType + '\'' +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OpenHabMqttMessage)) return false;

        OpenHabMqttMessage that = (OpenHabMqttMessage) o;

        if (site != null ? !site.equals(that.site) : that.site != null) return false;
        if (itemName != null ? !itemName.equals(that.itemName) : that.itemName != null) return false;
        if (eventType != null ? !eventType.equals(that.eventType) : that.eventType != null) return false;
        return !(value != null ? !value.equals(that.value) : that.value != null);

    }

    @Override
    public int hashCode() {
        int result = site != null ? site.hashCode() : 0;
        result = 31 * result + (itemName != null ? itemName.hashCode() : 0);
        result = 31 * result + (eventType != null ? eventType.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public abstract TimeSeriesDBMetricBuilder createDBMetricBuilder();

    public String getSite() {
        return site;
    }

    public String getItemName() {
        return itemName;
    }

    public String getEventType() {
        return eventType;
    }

    public Double getValue() {
        return value;
    }

    private String site;

    private String itemName;

    private String eventType;

    private Double value;
}
