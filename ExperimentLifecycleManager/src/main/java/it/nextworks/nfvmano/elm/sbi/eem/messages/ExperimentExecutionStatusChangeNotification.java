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
package it.nextworks.nfvmano.elm.sbi.eem.messages;

import it.nextworks.nfvmano.elm.im.ExperimentExecutionStatus;
import it.nextworks.nfvmano.libs.ifa.common.InterfaceMessage;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MalformattedElementException;

public class ExperimentExecutionStatusChangeNotification implements InterfaceMessage {

	private String experimentExecutionId;
	private ExperimentExecutionStatus currentStatus;
	
	public ExperimentExecutionStatusChangeNotification() { }
	
	public ExperimentExecutionStatusChangeNotification(String experimentExecutionId,
			ExperimentExecutionStatus currentStatus) { 
		this.experimentExecutionId = experimentExecutionId;
		this.currentStatus = currentStatus;
	}
	
	

	/**
	 * @return the experimentExecutionId
	 */
	public String getExperimentExecutionId() {
		return experimentExecutionId;
	}

	/**
	 * @return the currentStatus
	 */
	public ExperimentExecutionStatus getCurrentStatus() {
		return currentStatus;
	}

	@Override
	public void isValid() throws MalformattedElementException {
		if (experimentExecutionId == null) throw new MalformattedElementException("Notification without execution ID");
	}

}
