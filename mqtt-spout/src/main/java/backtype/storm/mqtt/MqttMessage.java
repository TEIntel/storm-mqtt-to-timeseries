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
package backtype.storm.mqtt;

/**
 * Represents an MQTT Message consisting of a topic string (e.g. "/users/ptgoetz/office/thermostat")
 * and a byte array message/payload.
 */
public class MqttMessage {
    public MqttMessage(String topic, byte[] payload) {
        this.topic = topic;
        this.message = payload;
    }

    public byte[] getMessage() {
        return this.message;
    }

    public String getTopic() {
        return this.topic;
    }

    private String topic;

    private byte[] message;
}
