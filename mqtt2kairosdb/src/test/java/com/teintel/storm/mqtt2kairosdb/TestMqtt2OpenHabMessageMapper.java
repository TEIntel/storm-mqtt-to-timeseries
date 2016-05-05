package com.teintel.storm.mqtt2kairosdb;

import java.math.BigDecimal;

import backtype.storm.mqtt.MqttMessage;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import com.teintel.storm.mqtt.MqttMessageMapper;
import com.teintel.storm.mqtt.openhab.OpenHabMqttMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by teveritt on March 06, 2016.
 */
public class TestMqtt2OpenHabMessageMapper {

    @Before
    public void setUp() throws Exception {
        message = mock(MqttMessage.class);
        mapper = new KairosMqttMessageMapper();
    }

    @Test
    public void test1() {
        assertTrue(NumberUtils.isNumber("5.85327100000000033475089367129839956760406494140625"));
        new BigDecimal("5.85327100000000033475089367129839956760406494140625");
    }

    @Test
    public void testItemOn1() throws Exception {
        String topic = TOPIC_PREFIX + "Switch_Wemo_1/state";
        String value = "ON";

        setupMessage(topic, value);
        assertFieldEquals("site", "site_name_123");
        assertFieldEquals("item_name", "switch.wemo_1");
        assertFieldEquals("event_type", "state");
        assertFieldEquals("value", new Double(1.0));
    }

    private void setupMessage(String topic, String value) {
        when(message.getTopic()).thenReturn(topic);
        when(message.getMessage()).thenReturn(value.getBytes());
    }

    private void assertFieldEquals(String field, Object expectedValue) {
        Values values = mapper.toValues(message);
        Fields fields = mapper.outputFields();

        Object actualValue = getValue(field, fields, values);

        assertEquals(expectedValue, actualValue);
    }

    private Object getValue(String field, Fields fields, Values values) {
        OpenHabMqttMessage message = (OpenHabMqttMessage) values.get(0);
        if (StringUtils.equals("site", field)) {
            return message.getSite();
        } else if (StringUtils.equals("item_name", field)) {
            return message.getItemName();
        } else if (StringUtils.equals("event_type", field)) {
            return message.getEventType();
        } else if (StringUtils.equals("value", field)) {
            return message.getValue();
        } else {
            return null;
        }
    }

    private static String TOPIC_PREFIX = "openhab/out/site_name_123/";

    private MqttMessage message;

    private MqttMessageMapper mapper;

        /*
Other possible test scenarios

openhab/out/site_name_123/Router1Device/state ON
openhab/out/site_name_123/Router1Latency/state 0.71811700000000000532196509084315039217472076416015625
openhab/out/site_name_123/Router1Time/state 0.71811700000000000532196509084315039217472076416015625
openhab/out/site_name_123/Router2Latency/state 5.88128999999999990677679306827485561370849609375
openhab/out/site_name_123/AxiomLatency/state 1.227381999999999973027797750546596944332122802734375
openhab/out/site_name_123/IPCamLandingLatency/state 4.22333900000000017627144188736565411090850830078125
openhab/out/site_name_123/NanoLatency/state 4.2308909999999997353370417840778827667236328125
openhab/out/site_name_123/Light_FF_Bath_Ceiling/state OFF
openhab/out/site_name_123/Switch_Wemo_1/state OFF
openhab/out/site_name_123/Light_FF_Bath_Ceiling/state OFF
openhab/out/site_name_123/Switch_Wemo_1/state OFF

openhab/out/site_name_123/Weather_LastUpdate/state 2016-03-05T02:17:25
openhab/out/site_name_123/CurrentDate/state 2016-03-05T02:17:25
openhab/out/site_name_123/Weather_LastUpdate/state 2016-03-05T02:17:25
     */
}
