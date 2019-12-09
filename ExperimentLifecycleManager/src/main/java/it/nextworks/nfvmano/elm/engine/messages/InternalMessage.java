/*
* Copyright 2018 Nextworks s.r.l.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package it.nextworks.nfvmano.elm.engine.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "msgType")
@JsonSubTypes({
	@Type(value = ScheduleExperimentInternalMessage.class, 	name = "SCHEDULE_REQUEST"),
	@Type(value = UpdateExperimentStateInternalMessage.class, 	name = "UPDATE_STATE_REQUEST"),
	@Type(value = DeployExperimentInternalMessage.class, 	name = "DEPLOY_EXPERIMENT_REQUEST"),
	@Type(value = NotifyNfvNsStatusChangeInternalMessage.class, 	name = "NFV_NS_STATUS_CHANGE_NOTIFICATION"),
	@Type(value = ExecuteExperimentInternalMessage.class, 	name = "EXECUTE_EXPERIMENT_REQUEST"),
	@Type(value = EemNotificationInternalMessage.class, name = "EEM_NOTIFICATION"),
	@Type(value = TerminateExperimentInternalMessage.class, name = "TERMINATE_EXPERIMENT_REQUEST"),

})
public abstract class InternalMessage {
	
	@JsonProperty("type")
	InternalMessageType type;

	/**
	 * @return the type
	 */
	public InternalMessageType getType() {
		return type;
	}

	
}
