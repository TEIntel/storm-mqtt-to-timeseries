package com.teintel.storm.mqtt2kairosdb;

import com.teintel.storm.mqtt.collectd.CollectdMetricBuilder;
import com.teintel.storm.mqtt.collectd.CollectdMqttMessage;
import org.apache.commons.lang3.StringUtils;
import org.kairosdb.client.builder.Metric;
import org.kairosdb.client.builder.MetricBuilder;

/**
 * Created by teveritt on March 06, 2016.
 */
public class KairosCollectdMetricBuilder extends CollectdMetricBuilder<MetricBuilder> implements KairosMetricBuilder {

    protected KairosCollectdMetricBuilder(CollectdMqttMessage message) {
        super(message);
    }

    @Override
    public MetricBuilder build() {
        boolean hasTime = message.getTime() != null && message.getTime() > 0;
        MetricBuilder metricBuilder = MetricBuilder.getInstance();

        for (String valueName : message.getValues().keySet()) {
            Double value = message.getValues().get(valueName);
            String metricName = getName();
            if (!StringUtils.equals("default", valueName)) {
                metricName += "." + valueName;
            }

            Metric metric = metricBuilder.addMetric(metricName)
                    .addTag("host", message.getHost())
                    .addTag("plugin", message.getPlugin())
                    .addTag("type", message.getType());

            if (StringUtils.isNotBlank(message.getPluginInstance())) {
                metric.addTag("plugin_instance", message.getPluginInstance());
            }

            if (StringUtils.isNotBlank(message.getTypeInstance())) {
                metric.addTag("type_instance", message.getTypeInstance());
            }


            if (hasTime) {
                metric.addDataPoint((long) message.getTime(), value);
            } else {
                metric.addDataPoint(value);
            }
        }


        return metricBuilder;
    }
}
