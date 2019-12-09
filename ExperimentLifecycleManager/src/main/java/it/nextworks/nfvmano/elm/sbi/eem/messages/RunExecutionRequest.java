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

import java.util.HashMap;
import java.util.Map;

import it.nextworks.nfvmano.libs.ifa.common.InterfaceMessage;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MalformattedElementException;

public class RunExecutionRequest implements InterfaceMessage {

	private String executionId;
	private String experimentDescriptorId;
	private Map<String, Map<String, String>> testCaseDescriptorConfiguration = new HashMap<String, Map<String,String>>();
	private String nsInstanceId;
	
	public RunExecutionRequest() {	}
	
	public RunExecutionRequest(String executionId,
			String experimentDescriptorId,
			Map<String, Map<String, String>> testCaseDescriptorConfiguration,
			String nsInstanceId) {
		this.executionId = executionId;
		this.experimentDescriptorId = experimentDescriptorId;
		if (testCaseDescriptorConfiguration != null) this.testCaseDescriptorConfiguration = testCaseDescriptorConfiguration;
		this.nsInstanceId = nsInstanceId;
	}

	
	
	/**
	 * @return the executionId
	 */
	public String getExecutionId() {
		return executionId;
	}

	/**
	 * @return the experimentDescriptorId
	 */
	public String getExperimentDescriptorId() {
		return experimentDescriptorId;
	}

	/**
	 * @return the testCaseDescriptorConfiguration
	 */
	public Map<String, Map<String, String>> getTestCaseDescriptorConfiguration() {
		return testCaseDescriptorConfiguration;
	}

	/**
	 * @return the nsInstanceId
	 */
	public String getNsInstanceId() {
		return nsInstanceId;
	}

	@Override
	public void isValid() throws MalformattedElementException {
		if (executionId == null) throw new MalformattedElementException("Run execution request without execution ID");
		if (experimentDescriptorId == null) throw new MalformattedElementException("Run execution request without expD ID");
		if (nsInstanceId == null) throw new MalformattedElementException("Run execution request without NS instance ID");
	}

}
