
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
package com.asyncapi.models;
  
import com.asyncapi.models.ModelContract;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;
public class DeviceRegistrationInfo  extends ModelContract{
  @JsonProperty(required = true)
  public UUID providerId;
  @JsonProperty(required = true)
  public UUID userId;
  public String authCode;
  @JsonProperty(required = true)
  public String sentAt;
  public DeviceRegistrationInfo(UUID providerId,UUID userId,String authCode,String sentAt) {
    
    this.providerId = providerId;
    
    this.userId = userId;
    
    this.authCode = authCode;
    
    this.sentAt = sentAt;
    
  }
  public DeviceRegistrationInfo() {
    super();
  }
}
