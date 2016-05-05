package com.teintel.storm.mqtt2influxdb;

import com.teintel.storm.mqtt.DBMessage;

/**
 * Created by teveritt on May 03, 2016.
 */
public interface InfluxMqttMessage extends DBMessage {
    @Override
    public InfluxMetricBuilder createDBMetricBuilder();
}
