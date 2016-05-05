package com.teintel.storm.mqtt2kairosdb;

import backtype.storm.mqtt.MqttMessage;
import com.teintel.storm.mqtt.collectd.CollectdMqttMessage;

/**
 * Created by teveritt on March 06, 2016.
 */
public class KairosCollectdMqttMessage extends CollectdMqttMessage implements KairosMqttMessage {

    public KairosCollectdMqttMessage(MqttMessage message) {
        super(message);
    }

    @Override
    public KairosCollectdMetricBuilder createDBMetricBuilder() {
        return new KairosCollectdMetricBuilder(this);
    }

    private static final long serialVersionUID = 127748096150339001L;
}
