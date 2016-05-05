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
package backtype.storm.mqtt.mappers;

import backtype.storm.mqtt.MqttMessage;
import backtype.storm.mqtt.MqttMessageMapper;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

/**
 * Given a String topic and byte[] message, emits a tuple with fields
 * "topic" and "message", both of which are Strings.
 */
public class StringMessageMapper implements MqttMessageMapper {
    public Values toValues(MqttMessage message) {
        return new Values(message.getTopic(), new String(message.getMessage()));
    }

    public Fields outputFields() {
        return new Fields("topic", "message");
    }
}
