package com.teintel.storm.mqtt2influxdb;

import backtype.storm.mqtt.MqttMessage;
import com.teintel.storm.mqtt.collectd.CollectdMqttMessage;

/**
 * Created by teveritt on March 06, 2016.
 */
public class InfluxCollectdMqttMessage extends CollectdMqttMessage implements InfluxMqttMessage {

    public InfluxCollectdMqttMessage(MqttMessage message) {
        super(message);
    }

    @Override
    public InfluxMetricBuilder createDBMetricBuilder() {
        return new InfluxCollectdMetricBuilder(this);
    }

    private static final long serialVersionUID = -2012564772635177528L;
}
