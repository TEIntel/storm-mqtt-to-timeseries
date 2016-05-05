package com.teintel.storm.mqtt2influxdb;

import backtype.storm.mqtt.MqttMessage;
import com.teintel.storm.mqtt.MqttMessageMapper;
import com.teintel.storm.mqtt.collectd.CollectdMqttMessage;
import com.teintel.storm.mqtt.openhab.OpenHabMqttMessage;

/**
 * Created by teveritt on May 03, 2016.
 */
public class InfluxMqttMessageMapper extends MqttMessageMapper {
    @Override
    public CollectdMqttMessage createCollectdMqttMessage(MqttMessage message) {
        return new InfluxCollectdMqttMessage(message);
    }

    @Override
    public OpenHabMqttMessage createOpenHabMqttMessage(MqttMessage message) {
        return new InfluxOpenHabMqttMessage(message);
    }
}
