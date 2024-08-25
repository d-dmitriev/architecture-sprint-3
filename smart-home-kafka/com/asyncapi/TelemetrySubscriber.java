
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
  
import java.time.Duration;
import java.util.logging.*;
import java.io.Serializable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.annotation.JsonView;

import com.asyncapi.ConnectionHelper;
import com.asyncapi.LoggingHelper;
import com.asyncapi.Connection;
import com.asyncapi.PubSubBase;

import com.asyncapi.models.ModelContract;
import com.asyncapi.models.Telemetry;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class TelemetrySubscriber  extends PubSubBase{

  private KafkaConsumer consumer = null;
    
  public TelemetrySubscriber() {
    
      super();
      String id = "my-subscriber";
      logger.info("Sub application is starting");

      this.createConnection("telemetry", id);

      consumer = ch.createConsumer("telemetry");
  
  }
  public void receive(int requestTimeout) {
    boolean continueProcessing = true;

    while (continueProcessing) {
        try {
            // receive message from Kafka
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(requestTimeout));

            for (ConsumerRecord<String, String> record : records) {

                logger.info("Received message: " + record.value());
                ObjectMapper o = new ObjectMapper();
                o.registerModule(new JavaTimeModule());
                Telemetry receivedObject = o.readValue(record.value(), Telemetry.class);
                logger.info("Received message type: " + receivedObject.getClass().getName());
                logger.info("Received message type: " + receivedObject.id + " , " + receivedObject.currentTemperature + " , " + receivedObject.lastUpdated);

                /*
                * Implement your business logic to handle
                * received messages here.
                */
            }
        } catch (Exception ex) {
            recordFailure(ex);
        }
    }
  }
  
  public void close() {
    consumer.close();
  }
  
}
