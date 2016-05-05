package com.teintel.storm.mqtt;

import backtype.storm.mqtt.MqttMessage;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import com.teintel.storm.mqtt.collectd.CollectdMqttMessage;
import com.teintel.storm.mqtt.openhab.OpenHabMqttMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class MqttMessageMapper implements backtype.storm.mqtt.MqttMessageMapper {
    public Values toValues(MqttMessage message) {
        String topic = message.getTopic();
        String[] topicElements = topic.split("/");
        String payload = new String(message.getMessage());

        LOG.info("Processing topic={}, payload={}", topic, payload);

        if (StringUtils.equals(topicElements[0], "collectd")) {
            return new Values(createCollectdMqttMessage(message));
        } else if (StringUtils.equals(topicElements[0], "openhab")) {
            return new Values(createOpenHabMqttMessage(message));
        } else {
            return null;
        }
    }

    public Fields outputFields() {
        return new Fields("message");
    }

    public abstract CollectdMqttMessage createCollectdMqttMessage(MqttMessage message);

    public abstract OpenHabMqttMessage createOpenHabMqttMessage(MqttMessage message);

    private static final Logger LOG = LoggerFactory.getLogger(MqttMessageMapper.class);
}
