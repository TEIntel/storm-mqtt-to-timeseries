package com.teintel.storm.mqtt2kairosdb;

import com.teintel.storm.mqtt.DBMessage;

/**
 * Created by teveritt on May 03, 2016.
 */
public interface KairosMqttMessage extends DBMessage {
    @Override
    public KairosMetricBuilder createDBMetricBuilder();
}
