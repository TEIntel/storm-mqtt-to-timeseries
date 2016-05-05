package com.teintel.storm.mqtt2influxdb;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.teintel.storm.mqtt.collectd.CollectdMetricBuilder;
import com.teintel.storm.mqtt.collectd.CollectdMqttMessage;
import org.apache.commons.lang3.StringUtils;
import org.influxdb.dto.Point;

/**
 * Created by teveritt on March 06, 2016.
 */
public class InfluxCollectdMetricBuilder extends CollectdMetricBuilder<List<Point>> implements InfluxMetricBuilder {

    protected InfluxCollectdMetricBuilder(CollectdMqttMessage message) {
        super(message);
    }

    @Override
    public List<Point> build() {
        boolean hasTime = message.getTime() != null && message.getTime() > 0;

        List<Point> points = new ArrayList<>(message.getValues().keySet().size());
        for (String valueName : message.getValues().keySet()) {
            Double value = message.getValues().get(valueName);
            String metricName = getName();
            if (!StringUtils.equals("default", valueName)) {
                metricName += "." + valueName;
            }

            Point.Builder pointBuilder = Point.measurement(metricName)
                    .addField("host", message.getHost())
                    .addField("plugin", message.getPlugin())
                    .addField("type", message.getType())
                    .addField("value", value);

            if (StringUtils.isNotBlank(message.getPluginInstance())) {
                pointBuilder.addField("plugin_instance", message.getPluginInstance());
            }

            if (StringUtils.isNotBlank(message.getTypeInstance())) {
                pointBuilder.addField("type_instance", message.getTypeInstance());
            }

            long timeMS = System.currentTimeMillis();
            if (hasTime) {
                timeMS = message.getTime();
            }

            pointBuilder.time(timeMS, TimeUnit.MILLISECONDS);
            points.add(pointBuilder.build());
        }

        return points;
    }
}
