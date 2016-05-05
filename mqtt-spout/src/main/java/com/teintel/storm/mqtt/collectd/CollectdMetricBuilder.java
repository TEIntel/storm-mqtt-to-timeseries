package com.teintel.storm.mqtt.collectd;

import java.util.LinkedHashSet;
import java.util.Set;

import com.teintel.storm.mqtt.TimeSeriesDBMetricBuilder;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by teveritt on March 06, 2016.
 */
public abstract class CollectdMetricBuilder<T> implements TimeSeriesDBMetricBuilder<T> {
    protected CollectdMetricBuilder(CollectdMqttMessage message) {
        this.message = message;

        if (message.getValues().isEmpty()) {
            throw new IllegalArgumentException("At least one value needs to be set: " + message);
        }

        String host = message.getHost();
        String plugin = message.getPlugin();
        String plugin_instance = message.getPluginInstance();
        String type = message.getType();
        String type_instance = message.getTypeInstance();

        Set<String> nameBuilder = new LinkedHashSet<>();

        if (StringUtils.isNotBlank(host)) {
            nameBuilder.add(host);
        }
        if (StringUtils.isNotBlank(plugin)) {
            nameBuilder.add(plugin);
        }
        if (StringUtils.isNotBlank(plugin_instance)) {
            nameBuilder.add(plugin_instance);
        }
        if (StringUtils.isNotBlank(type)) {
            nameBuilder.add(type);
        }
        if (StringUtils.isNotBlank(type_instance)) {
            nameBuilder.add(type_instance);
        }

        String metricName = "";
        for (String n : nameBuilder) {
            if (StringUtils.isNotBlank(metricName)) {
                metricName += ".";
            }
            metricName += n;
        }

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

    protected CollectdMqttMessage message;
}
