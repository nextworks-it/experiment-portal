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

import java.util.HashMap;
import java.util.Map;

import it.nextworks.nfvmano.libs.ifa.common.InterfaceMessage;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MalformattedElementException;

public class ExecuteExperimentRequest implements InterfaceMessage {

	private String experimentId;
	
	private String executionName;
	
	//this map specifies the test cases for the requested execution 
	//in general a subset of the test cases can be executed in each run and the config parameters of the descriptors can be overwritten   
	//the key of the map is the test case descriptor ID
	//the value of the map is a map with the user parameters to be overwritten for the given run
	//the format of the map in the value field is the same as in the test case descriptor: 
	//Key: parameter name, as in the key of the corresponding map in the blueprint; value: desired value
	//Important note: this field is optional, i.e. if not provided all the test cases will be executed by default, with the configuration given in the descriptor
	private Map<String, Map<String, String>> testCaseDescriptorConfiguration = new HashMap<String, Map<String,String>>();
	
	public ExecuteExperimentRequest() {}
	
	public ExecuteExperimentRequest(String experimentId, 
			Map<String, Map<String, String>> testCaseDescriptorConfiguration,
			String executionName) {
		this.experimentId = experimentId;
		if (testCaseDescriptorConfiguration != null) this.testCaseDescriptorConfiguration = testCaseDescriptorConfiguration;
		this.executionName = executionName;
	}
	
		
	/**
	 * @return the experimentId
	 */
	public String getExperimentId() {
		return experimentId;
	}

	/**
	 * @return the testCaseDescriptorConfiguration
	 */
	public Map<String, Map<String, String>> getTestCaseDescriptorConfiguration() {
		return testCaseDescriptorConfiguration;
	}
	
	

	/**
	 * @return the executionName
	 */
	public String getExecutionName() {
		return executionName;
	}

	@Override
	public void isValid() throws MalformattedElementException {
		if (experimentId == null) throw new MalformattedElementException("Experiment execution request without experiment ID.");
		if (executionName == null) throw new MalformattedElementException("Experiment execution request without execution name.");
	}

}
