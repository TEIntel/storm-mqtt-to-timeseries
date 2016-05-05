package com.teintel.storm.mqtt2kairosdb;

import java.util.List;

import backtype.storm.mqtt.MqttMessage;
import com.teintel.storm.mqtt.openhab.OpenHabMqttMessage;
import org.junit.Test;
import org.kairosdb.client.builder.DataPoint;
import org.kairosdb.client.builder.Metric;
import org.kairosdb.client.builder.MetricBuilder;

import static org.junit.Assert.assertEquals;

/**
 * Created by teveritt on March 31, 2016.
 */
public class TestOpenHabMetricBuilder {

    @Test
    public void testWemoOff() {
        String topic = "openhab/out/site_name_123/Switch_Wemo_1/state";
        String payload = "OFF";
        Double expectedValue = 0.0;

        OpenHabMqttMessage message = new KairosOpenHabMqttMessage(new MqttMessage(topic, payload.getBytes()));

        KairosOpenHabMetricBuilder builder = new KairosOpenHabMetricBuilder(message);
        MetricBuilder metricBuilder = builder.build();
        List<Metric> metrics = metricBuilder.getMetrics();
        assertEquals(1, metrics.size());

        Metric metric = metrics.get(0);
        assertEquals("site_name_123.switch.wemo_1-state", metric.getName());
        assertEquals(3, metric.getTags().size());
        assertEquals(1, metric.getDataPoints().size());
        DataPoint dataPoint = metric.getDataPoints().get(0);
        assertEquals("site_name_123", metric.getTags().get("site"));
        assertEquals("switch.wemo_1", metric.getTags().get("item_name"));
        assertEquals("state", metric.getTags().get("event_type"));
        assertEquals(expectedValue, dataPoint.getValue());

    }

    @Test
    public void testWemoOn() {
        String topic = "openhab/out/site_name_123/Switch_Wemo_1/state";
        String payload = "ON";
        Double expectedValue = 1.0;

        OpenHabMqttMessage message = new KairosOpenHabMqttMessage(new MqttMessage(topic, payload.getBytes()));

        KairosOpenHabMetricBuilder builder = new KairosOpenHabMetricBuilder(message);
        MetricBuilder metricBuilder = builder.build();
        List<Metric> metrics = metricBuilder.getMetrics();
        assertEquals(1, metrics.size());

        Metric metric = metrics.get(0);
        assertEquals("site_name_123.switch.wemo_1-state", metric.getName());
        assertEquals(3, metric.getTags().size());
        assertEquals(1, metric.getDataPoints().size());
        DataPoint dataPoint = metric.getDataPoints().get(0);
        assertEquals("site_name_123", metric.getTags().get("site"));
        assertEquals("switch.wemo_1", metric.getTags().get("item_name"));
        assertEquals("state", metric.getTags().get("event_type"));
        assertEquals(expectedValue, dataPoint.getValue());

    }
}
