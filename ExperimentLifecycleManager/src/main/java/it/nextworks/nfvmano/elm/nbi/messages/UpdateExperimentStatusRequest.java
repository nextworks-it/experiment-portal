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

import it.nextworks.nfvmano.elm.im.ExperimentStatus;
import it.nextworks.nfvmano.libs.ifa.common.InterfaceMessage;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MalformattedElementException;

public class UpdateExperimentStatusRequest implements InterfaceMessage {

	private String experimentId;
	private ExperimentStatus status;
	
	public UpdateExperimentStatusRequest() { }
	
	public UpdateExperimentStatusRequest(String experimentId,
			ExperimentStatus status) {
		this.experimentId = experimentId;
		this.status = status;
	}
	
	

	/**
	 * @return the experimentId
	 */
	public String getExperimentId() {
		return experimentId;
	}

	/**
	 * @return the status
	 */
	public ExperimentStatus getStatus() {
		return status;
	}

	@Override
	public void isValid() throws MalformattedElementException {
		if (experimentId == null) 
			throw new MalformattedElementException("Update experiment request status without experiment ID.");
		if (status == null)
			throw new MalformattedElementException("Update experiment request status without target status.");
	}

}
