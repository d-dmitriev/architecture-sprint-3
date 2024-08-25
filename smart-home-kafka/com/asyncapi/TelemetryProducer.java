
/*
* (c) Copyright IBM Corporation 2021
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.asyncapi;
  
import java.util.logging.*;
import java.io.Serializable;
import java.util.UUID;

import com.asyncapi.ConnectionHelper;
import com.asyncapi.LoggingHelper;
import com.asyncapi.Connection;
import com.asyncapi.PubSubBase;
import com.asyncapi.models.ModelContract;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.annotation.JsonView;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
  
import com.asyncapi.models.Telemetry;
import com.asyncapi.models.DeviceRegistrationInfo;
public class TelemetryProducer  extends PubSubBase{

    private KafkaProducer producer = null;
  
  public TelemetryProducer() {
    
    super();
    String id = "my-publisher";

    logger.info("Pub application is starting");

    // prepare connection for producer
    createConnection("telemetry", id);

    producer = ch.createProducer();

  }
    public void send(ModelContract modelContract) {
        Serializable modelInstance = (Serializable) modelContract;

        try{
          // JSON encode and transmit
          ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
          String json = ow.writeValueAsString(modelInstance);

          logger.info("Sending Message: " + json);

          ProducerRecord<String, String> record = new ProducerRecord<String, String>(topicName, json);
          producer.send(record);

        }catch (Exception e){
          logger.severe("An error occured whilst attempting to send a message: " + e);
        }
    }
    public void close() {
      producer.close();
    }
  
}
