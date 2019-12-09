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
package it.nextworks.nfvmano.elm.im;

import java.time.OffsetDateTime;

import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.nextworks.nfvmano.libs.ifa.common.DescriptorInformationElement;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MalformattedElementException;

@Embeddable
public class ExperimentExecutionTimeslot implements DescriptorInformationElement {


	@JsonProperty("startTime")
	private OffsetDateTime startTime;


	@JsonProperty("stopTime")
	private OffsetDateTime stopTime;

	public ExperimentExecutionTimeslot() {
		// JPA only
	}

	@JsonCreator
	public ExperimentExecutionTimeslot(
			@JsonProperty("startTime") OffsetDateTime startTime,
			@JsonProperty("stopTime") OffsetDateTime stopTime) {
		this.startTime = startTime;
		this.stopTime = stopTime;
	}

	/**
	 * @return the startTime
	 */
	public OffsetDateTime getStartTime() {
		return startTime;
	}

	/**
	 * @return the stopTime
	 */
	public OffsetDateTime getStopTime() {
		return stopTime;
	}
	
	@JsonIgnore
	public boolean includeTimeInstance(OffsetDateTime time) {
		if (time.isAfter(startTime) && time.isBefore(stopTime)) return true;
		else return false;
	}
	
	@Override
	public void isValid() throws MalformattedElementException {
		if (startTime == null) 
			throw new MalformattedElementException("Experiment execution timeslot without start time.");
		if (stopTime == null) 
			throw new MalformattedElementException("Experiment execution timeslot without stop time.");
		if (stopTime.isBefore(startTime)) 
			throw new MalformattedElementException("Experiment execution timeslot with stop time antecedent start time");
	}

}
