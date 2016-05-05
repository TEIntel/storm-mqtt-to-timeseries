package com.teintel.storm.mqtt2kairosdb;

import backtype.storm.mqtt.MqttMessage;
import com.teintel.storm.mqtt.openhab.OpenHabMqttMessage;

/**
 * Created by teveritt on March 06, 2016.
 */
public class KairosOpenHabMqttMessage extends OpenHabMqttMessage implements KairosMqttMessage {

    public KairosOpenHabMqttMessage(MqttMessage message) {
        super(message);
    }

    @Override
    public KairosOpenHabMetricBuilder createDBMetricBuilder() {
        return new KairosOpenHabMetricBuilder(this);
    }

    private static final long serialVersionUID = 5661024499701053883L;
}
