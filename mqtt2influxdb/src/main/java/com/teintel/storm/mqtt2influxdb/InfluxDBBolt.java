package com.teintel.storm.mqtt2influxdb;

import java.util.List;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by teveritt on March 03, 2016.
 */
public class InfluxDBBolt extends BaseRichBolt {
    public InfluxDBBolt(InfluxDBOptions options) {
        this.options = options;
    }

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {

        Object m = tuple.getValueByField("message");
        if (!(m instanceof InfluxMqttMessage)) {
            return;  // Skip it
        }

        InfluxMqttMessage message = (InfluxMqttMessage) m;
        InfluxMetricBuilder builder;

        try {
            LOG.debug("Building " + message);
            builder = message.createDBMetricBuilder();
        } catch (Exception e) {
            collector.reportError(e);
            return;
        }

        List<Point> points = builder.build();


        if (savePoints(points) == -1) {
            collector.fail(tuple);
        } else {

            // LOG.info(String.format("Saved to InfluxDB metric=%s time=%s value=%s",
            //        metricName, time, value));

            collector.ack(tuple);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }


    private int savePoints(List<Point> points) {
        try {
            InfluxDB influxDB = InfluxDBFactory.connect(options.getUrl(), options.getUsername(), options.getPassword());

            BatchPoints batchPoints = BatchPoints
                    .database(options.getDatabase())
                    .tag("async", options.getAsync())
                    .retentionPolicy(options.getRetentionPolicy())
                    .consistency(InfluxDB.ConsistencyLevel.ALL)
                    .points(points.toArray(new Point[points.size()]))
                    .build();

            influxDB.write(batchPoints);
            return 1;
        } catch (Throwable e) {
            LOG.error(e.getMessage(), e);
            return -1;
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(InfluxDBBolt.class);

    private InfluxDBOptions options;

    private OutputCollector collector;
}
