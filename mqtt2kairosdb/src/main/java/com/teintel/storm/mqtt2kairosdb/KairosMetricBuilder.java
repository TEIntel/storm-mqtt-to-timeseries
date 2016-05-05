package com.teintel.storm.mqtt2kairosdb;

import com.teintel.storm.mqtt.TimeSeriesDBMetricBuilder;
import org.kairosdb.client.builder.MetricBuilder;

/**
 * Created by teveritt on May 03, 2016.
 */
public interface KairosMetricBuilder extends TimeSeriesDBMetricBuilder<MetricBuilder> {

    public MetricBuilder build();
}
