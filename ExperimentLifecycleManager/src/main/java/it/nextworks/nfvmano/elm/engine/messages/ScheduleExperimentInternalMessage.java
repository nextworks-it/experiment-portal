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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.nextworks.nfvmano.elm.nbi.messages.ExperimentSchedulingRequest;

public class ScheduleExperimentInternalMessage extends InternalMessage {

	@JsonProperty("experimentId")
	private String experimentId;

	@JsonProperty("requester")
	private String requester;
	
	@JsonProperty("request")
	private ExperimentSchedulingRequest request;
	
	@JsonCreator
	public ScheduleExperimentInternalMessage(@JsonProperty("experimentId") String experimentId,
			@JsonProperty("request") ExperimentSchedulingRequest request, @JsonProperty("requester") String requester) {
		this.type = InternalMessageType.SCHEDULE_REQUEST;
		this.experimentId = experimentId;
		this.request = request;
		this.requester =requester;
	}

	/**
	 * @return the experimentId
	 */
	public String getExperimentId() {
		return experimentId;
	}

	/**
	 * @return the request
	 */
	public ExperimentSchedulingRequest getRequest() {
		return request;
	}



	/**
	 * @return the requester
	 */
	public String getRequester() {
		return requester;
	}
	
}
