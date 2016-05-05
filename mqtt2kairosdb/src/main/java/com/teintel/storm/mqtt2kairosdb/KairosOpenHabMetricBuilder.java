package com.teintel.storm.mqtt2kairosdb;

import com.teintel.storm.mqtt.openhab.OpenHabMetricBuilder;
import com.teintel.storm.mqtt.openhab.OpenHabMqttMessage;
import org.apache.commons.lang3.StringUtils;
import org.kairosdb.client.builder.Metric;
import org.kairosdb.client.builder.MetricBuilder;

/**
 * Created by teveritt on March 06, 2016.
 */
public class KairosOpenHabMetricBuilder extends OpenHabMetricBuilder<MetricBuilder> implements KairosMetricBuilder {

    protected KairosOpenHabMetricBuilder(OpenHabMqttMessage message) {
        super(message);
    }

    @Override
    public MetricBuilder build() {
        MetricBuilder metricBuilder = MetricBuilder.getInstance();


        Metric metric = metricBuilder.addMetric(name)
                .addTag("site", message.getSite());

        if (StringUtils.isNotBlank(message.getItemName())) {
            metric.addTag("item_name", message.getItemName());
        }

        if (StringUtils.isNotBlank(message.getEventType())) {
            metric.addTag("event_type", message.getEventType());
        }

        metric.addDataPoint(message.getValue());

        return metricBuilder;
    }
}
