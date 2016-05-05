/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package backtype.storm.mqtt.bolt;

import java.util.Map;

import backtype.storm.Config;
import backtype.storm.mqtt.MqttMessage;
import backtype.storm.mqtt.MqttTupleMapper;
import backtype.storm.mqtt.common.MqttOptions;
import backtype.storm.mqtt.common.MqttPublisher;
import backtype.storm.mqtt.common.SslUtils;
import backtype.storm.mqtt.ssl.KeyStoreLoader;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import backtype.storm.utils.TupleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MqttBolt extends BaseRichBolt {
    public MqttBolt(MqttOptions options, MqttTupleMapper mapper) {
        this(options, mapper, null, false);
    }

    public MqttBolt(MqttOptions options, MqttTupleMapper mapper, boolean retain) {
        this(options, mapper, null, retain);
    }

    public MqttBolt(MqttOptions options, MqttTupleMapper mapper, KeyStoreLoader keyStoreLoader) {
        this(options, mapper, keyStoreLoader, false);
    }

    public MqttBolt(MqttOptions options, MqttTupleMapper mapper, KeyStoreLoader keyStoreLoader, boolean retain) {
        this.options = options;
        this.mapper = mapper;
        this.retain = retain;
        this.keyStoreLoader = keyStoreLoader;
        // the following code is duplicated in the constructor of MqttPublisher
        // we reproduce it here so we fail on the client side if SSL is misconfigured, rather than when the topology
        // is deployed to the cluster
        SslUtils.checkSslConfig(this.options.getUrl(), keyStoreLoader);
    }

    @Override
    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.topologyName = (String) conf.get(Config.TOPOLOGY_NAME);
        this.publisher = new MqttPublisher(this.options, this.keyStoreLoader, this.retain);
        try {
            this.publisher.connectMqtt(this.topologyName + "-" + context.getThisComponentId() + "-" + context.getThisTaskId());
        } catch (Exception e) {
            LOG.error("Unable to connect to MQTT Broker.", e);
            throw new RuntimeException("Unable to connect to MQTT Broker.", e);
        }
    }

    @Override
    public void execute(Tuple input) {
        //ignore tick tuples
        if (!TupleUtils.isTick(input)) {
            MqttMessage message = this.mapper.toMessage(input);
            try {
                this.publisher.publish(message);
                this.collector.ack(input);
            } catch (Exception e) {
                LOG.warn("Error publishing MQTT message. Failing tuple.", e);
                // should we fail the tuple or kill the worker?
                collector.reportError(e);
                collector.fail(input);
            }
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // this bolt does not emit tuples
    }

    private static final Logger LOG = LoggerFactory.getLogger(MqttBolt.class);

    private MqttTupleMapper mapper;

    private transient MqttPublisher publisher;

    private boolean retain = false;

    private transient OutputCollector collector;

    private MqttOptions options;

    private KeyStoreLoader keyStoreLoader;

    private transient String topologyName;
}
