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
package it.nextworks.nfvmano.elm.nbi.messages;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.nextworks.nfvmano.catalogue.blueprint.elements.EveSite;
import it.nextworks.nfvmano.elm.im.ExperimentExecutionTimeslot;
import it.nextworks.nfvmano.libs.ifa.common.InterfaceMessage;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MalformattedElementException;

public class ExperimentSchedulingRequest implements InterfaceMessage {

	@JsonProperty("experimentDescriptorId")
	private String experimentDescriptorId;

	@JsonProperty("proposedTimeSlot")
	private ExperimentExecutionTimeslot proposedTimeSlot;

	@JsonProperty("targetSites")
	private List<EveSite> targetSites = new ArrayList<EveSite>();
	
	public ExperimentSchedulingRequest() {	}

	@JsonCreator
	public ExperimentSchedulingRequest(@JsonProperty("experimentDescriptorId") String experimentDescriptorId,
									   @JsonProperty("proposedTimeSlot") ExperimentExecutionTimeslot proposedTimeSlot,
									   @JsonProperty("targetSites")List<EveSite> targetSites) {
		this.experimentDescriptorId = experimentDescriptorId;
		this.proposedTimeSlot = proposedTimeSlot;
		if (targetSites != null) this.targetSites = targetSites;
	}
	
	

	/**
	 * @return the targetSites
	 */
	public List<EveSite> getTargetSites() {
		return targetSites;
	}

	/**
	 * @return the experimentDescriptorId
	 */
	public String getExperimentDescriptorId() {
		return experimentDescriptorId;
	}

	/**
	 * @return the proposedTimeSlot
	 */
	public ExperimentExecutionTimeslot getProposedTimeSlot() {
		return proposedTimeSlot;
	}

	@Override
	public void isValid() throws MalformattedElementException {
		if (experimentDescriptorId == null) 
			throw new MalformattedElementException("Experiment scheduling request without experiment descriptor ID");
		if (proposedTimeSlot == null) 
			throw new MalformattedElementException("Experiment scheduling request without timeslot");
		else proposedTimeSlot.isValid();
		if (targetSites.isEmpty()) 
			throw new MalformattedElementException("Experiment scheduling request without target site");
	}
	
}
