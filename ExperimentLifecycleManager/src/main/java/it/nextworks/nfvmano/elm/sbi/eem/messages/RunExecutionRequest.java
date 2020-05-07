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
import java.util.List;
import java.util.ArrayList;


import it.nextworks.nfvmano.libs.ifa.common.InterfaceMessage;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MalformattedElementException;

public class RunExecutionRequest implements InterfaceMessage {

	private String executionId;
	private String experimentDescriptorId;
	private Map<String, Map<String, String>> testCaseDescriptorConfiguration = new HashMap<String, Map<String,String>>();
	private String nsInstanceId;
	private String tenantId;
	private List<String> siteNames = new ArrayList<>();
	private String experimentId;
	private String useCase;
	
	public RunExecutionRequest() {	}
	
	public RunExecutionRequest(String executionId,
			String experimentDescriptorId,
			Map<String, Map<String, String>> testCaseDescriptorConfiguration,
			String nsInstanceId,
            String tenantId,
			List<String> siteNames,
			String experimentId,
							   String useCase) {
		this.executionId = executionId;
		this.experimentDescriptorId = experimentDescriptorId;
		if (testCaseDescriptorConfiguration != null) this.testCaseDescriptorConfiguration = testCaseDescriptorConfiguration;
		this.nsInstanceId = nsInstanceId;
		this.tenantId = tenantId;
		this.experimentId = experimentId;
		if (siteNames != null){
			for (String siteName: siteNames){
				this.siteNames.add(siteName);
			}
		}
		this.useCase=useCase;
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

	public String getTenantId(){ return tenantId; }

	/**
	 * @return the nsInstanceId
	 */
	public String getNsInstanceId() {
		return nsInstanceId;
	}


	public String getExperimentId(){ return experimentId; }

	public void setExecutionId(String experimentId){ this.experimentId = experimentId; }

	public List<String> getSiteNames(){ return this.siteNames; }

	public void setSiteNames(List<String> siteNames){
		if (siteNames != null){
			for (String site: siteNames)
				this.siteNames.add(site);
		}
	}

	public String getUseCase(){ return useCase;	}


	@Override
	public void isValid() throws MalformattedElementException {
		if (executionId == null) throw new MalformattedElementException("Run execution request without execution ID");
		if (experimentDescriptorId == null) throw new MalformattedElementException("Run execution request without expD ID");
		if (nsInstanceId == null) throw new MalformattedElementException("Run execution request without NS instance ID");
		if (tenantId == null) throw new MalformattedElementException("Run execution request without tenant ID");
		if (experimentId == null) throw new MalformattedElementException("Run execution request without experiment ID");
		if (useCase == null) throw new MalformattedElementException("Run execution request without use case");
	}

}
