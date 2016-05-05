package com.teintel.storm.mqtt.openhab;

import com.teintel.storm.mqtt.TimeSeriesDBMetricBuilder;

/**
 * Created by teveritt on March 06, 2016.
 */
public abstract class OpenHabMetricBuilder<T> implements TimeSeriesDBMetricBuilder<T> {
    protected OpenHabMetricBuilder(OpenHabMqttMessage message) {
        this.message = message;
        Double value = message.getValue();

        if (value == null) {
            throw new IllegalArgumentException("Value from tuple can't be null: " + message);
        }

        String site = message.getSite();
        String eventType = message.getEventType();
        String itemName = message.getItemName();

        String metricName = String.format("%s.%s-%s", site, itemName, eventType);

        setName(metricName);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public abstract T build();

    protected String name;

    protected OpenHabMqttMessage message;
}
