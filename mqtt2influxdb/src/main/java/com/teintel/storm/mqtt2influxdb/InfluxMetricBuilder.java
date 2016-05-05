package com.teintel.storm.mqtt2influxdb;

import java.util.List;

import com.teintel.storm.mqtt.TimeSeriesDBMetricBuilder;
import org.influxdb.dto.Point;

/**
 * Created by teveritt on May 03, 2016.
 */
public interface InfluxMetricBuilder extends TimeSeriesDBMetricBuilder<List<Point>> {

    public List<Point> build();
}
