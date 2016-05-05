package com.teintel.storm.mqtt2influxdb;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.teintel.storm.mqtt.openhab.OpenHabMetricBuilder;
import com.teintel.storm.mqtt.openhab.OpenHabMqttMessage;
import org.apache.commons.lang3.StringUtils;
import org.influxdb.dto.Point;

/**
 * Created by teveritt on March 06, 2016.
 */
public class InfluxOpenHabMetricBuilder extends OpenHabMetricBuilder<List<Point>> implements InfluxMetricBuilder {

    protected InfluxOpenHabMetricBuilder(OpenHabMqttMessage message) {
        super(message);
    }

    @Override
    public List<Point> build() {

        Point.Builder pointBuilder = Point.measurement(name)
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("site", message.getSite())
                .addField("value", message.getValue());

        if (StringUtils.isNotBlank(message.getItemName())) {
            pointBuilder.addField("item_name", message.getItemName());
        }

        if (StringUtils.isNotBlank(message.getEventType())) {
            pointBuilder.addField("event_type", message.getEventType());
        }

        List<Point> points = new ArrayList<>(1);
        points.add(pointBuilder.build());

        return points;
    }
}
