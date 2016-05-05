package com.teintel.storm.mqtt2influxdb;

import backtype.storm.mqtt.MqttMessage;
import com.teintel.storm.mqtt.openhab.OpenHabMqttMessage;

/**
 * Created by teveritt on March 06, 2016.
 */
public class InfluxOpenHabMqttMessage extends OpenHabMqttMessage implements InfluxMqttMessage {

    public InfluxOpenHabMqttMessage(MqttMessage message) {
        super(message);
    }

    @Override
    public InfluxOpenHabMetricBuilder createDBMetricBuilder() {
        return new InfluxOpenHabMetricBuilder(this);
    }

    private static final long serialVersionUID = -5280546779832135639L;
}
