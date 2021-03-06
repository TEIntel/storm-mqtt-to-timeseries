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


import java.io.Serializable;

import backtype.storm.tuple.ITuple;

/**
 * Given a Tuple, converts it to an MQTT message.
 */
public interface MqttTupleMapper extends Serializable {

    /**
     * Converts a Tuple to a MqttMessage
     *
     * @param tuple the incoming tuple
     * @return the message to publish
     */
    MqttMessage toMessage(ITuple tuple);

}
