package com.teintel.storm.mqtt2kairosdb;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import org.kairosdb.client.HttpClient;
import org.kairosdb.client.builder.MetricBuilder;
import org.kairosdb.client.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by teveritt on March 03, 2016.
 */
public class KairosDBBolt extends BaseRichBolt {
    public KairosDBBolt(KairosDBOptions options) {
        this.options = options;
    }

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {

        Object m = tuple.getValueByField("message");
        if (!(m instanceof KairosCollectdMqttMessage)) {
            return;  // Skip it
        }

        KairosCollectdMqttMessage message = (KairosCollectdMqttMessage) m;
        KairosCollectdMetricBuilder kairosDBMetric;

        try {
            LOG.debug("Building " + message);
            kairosDBMetric = message.createDBMetricBuilder();
        } catch (Exception e) {
            collector.reportError(e);
            return;
        }

        MetricBuilder builder = kairosDBMetric.build();


        if (saveMetric(builder) == -1) {
            collector.fail(tuple);
        } else {

            // LOG.info(String.format("Saved to KairosDB metric=%s time=%s value=%s",
            //        metricName, time, value));

            collector.ack(tuple);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }

    /**
     * Saves a metric to KairosDB
     *
     * @param metricBuilder The metric builder to send to KairosDB
     * @return -1 if successful, otherwise status code
     */
    private int saveMetric(MetricBuilder metricBuilder) {

        Response response = null;
        HttpClient client = null;

        try {

            client = new HttpClient(options.getUrl());


            response = client.pushMetrics(metricBuilder);

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            try {
                if (client != null)
                    client.shutdown();
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }

        if (response != null) {

            int statusCode = response.getStatusCode();

            if (statusCode != 204) {

                List<String> errors = response.getErrors();
                if (errors.size() > 0) {
                    LOG.error(errors.get(0));
                } else {
                    LOG.error("saveMetric '" + metricBuilder.getMetrics().iterator().next()
                                      + "' response status code is "
                                      + response.getStatusCode());
                }

                return -1;

            } else {
                return statusCode;
            }

        } else {
            return -1;
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(KairosDBBolt.class);

    private KairosDBOptions options;

    private OutputCollector collector;
}
