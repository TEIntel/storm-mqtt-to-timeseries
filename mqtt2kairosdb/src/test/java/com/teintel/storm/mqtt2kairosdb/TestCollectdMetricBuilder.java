package com.teintel.storm.mqtt2kairosdb;

import java.util.List;

import backtype.storm.mqtt.MqttMessage;
import com.teintel.storm.mqtt.collectd.CollectdMqttMessage;
import org.junit.Test;
import org.kairosdb.client.builder.DataPoint;
import org.kairosdb.client.builder.Metric;
import org.kairosdb.client.builder.MetricBuilder;

import static org.junit.Assert.assertEquals;

/**
 * Created by teveritt on March 31, 2016.
 */
public class TestCollectdMetricBuilder {

    @Test
    public void testInterfacePackets() {
        String topic = "collectd/carlingford/interface-eth0/if_packets";
        String payload = "1459362240.409:6.89932181993374:9.79903678773197";
        Long expectedTime = 1459362240409l;
        Double expectedRx = 6.89932181993374;
        Double expectedTx = 9.79903678773197;

        CollectdMqttMessage message = new KairosCollectdMqttMessage(new MqttMessage(topic, payload.getBytes()));

        KairosCollectdMetricBuilder builder = new KairosCollectdMetricBuilder(message);
        MetricBuilder metricBuilder = builder.build();
        List<Metric> metrics = metricBuilder.getMetrics();
        assertEquals(2, metrics.size());

        Metric metricRx = metrics.get(0);
        assertEquals("carlingford.interface.eth0.if_packets.rx", metricRx.getName());
        assertEquals(4, metricRx.getTags().size());
        assertEquals(1, metricRx.getDataPoints().size());
        DataPoint dataPointRx = metricRx.getDataPoints().get(0);
        assertEquals("carlingford", metricRx.getTags().get("host"));
        assertEquals("interface", metricRx.getTags().get("plugin"));
        assertEquals("if_packets", metricRx.getTags().get("type"));
        assertEquals("eth0", metricRx.getTags().get("plugin_instance"));
        assertEquals(expectedTime, new Long(dataPointRx.getTimestamp()));
        assertEquals(expectedRx, dataPointRx.getValue());


        Metric metricTx = metrics.get(1);
        assertEquals("carlingford.interface.eth0.if_packets.tx", metricTx.getName());
        assertEquals(4, metricTx.getTags().size());
        assertEquals(1, metricTx.getDataPoints().size());
        DataPoint dataPointTx = metricTx.getDataPoints().get(0);
        assertEquals("carlingford", metricTx.getTags().get("host"));
        assertEquals("interface", metricTx.getTags().get("plugin"));
        assertEquals("if_packets", metricTx.getTags().get("type"));
        assertEquals("eth0", metricTx.getTags().get("plugin_instance"));
        assertEquals(expectedTime, new Long(dataPointTx.getTimestamp()));
        assertEquals(expectedTx, dataPointTx.getValue());

    }
}
