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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
public class ExperimentExecution {

	@Id
    @GeneratedValue
    @JsonIgnore
    private Long id;
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.EAGER)
	private Experiment experiment;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String executionId;

	private ExperimentExecutionStatus status;

	//this list specifies the test cases for the requested execution 
	//in general a subset of the test cases can be executed in each run and the config parameters of the descriptors can be overwritten   
	//Important note: this field is optional, i.e. if not provided all the test cases will be executed by default, with the configuration given in the descriptor
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy = "execution", cascade=CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<TestCaseExecutionConfiguration> testCaseDescriptorConfiguration = new ArrayList<TestCaseExecutionConfiguration>();
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@ElementCollection(fetch=FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private Map<String, ExecutionResult> testCaseResult = new HashMap<>();
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String reportUrl;
	
	@JsonIgnore
	private String eemSubscriptionId;
	
	public ExperimentExecution() { }
	
	public ExperimentExecution(Experiment experiment,
			String executionId,
			String eemSubscriptionId) { 
		this.experiment = experiment;
		this.executionId = executionId;
		this.status = ExperimentExecutionStatus.INIT;
		this.eemSubscriptionId = eemSubscriptionId;
	}

	/**
	 * @return the status
	 */
	public ExperimentExecutionStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(ExperimentExecutionStatus status) {
		this.status = status;
	}

	/**
	 * @return the eemSubscriptionId
	 */
	public String getEemSubscriptionId() {
		return eemSubscriptionId;
	}

	
	
	/**
	 * @return the experiment
	 */
	public Experiment getExperiment() {
		return experiment;
	}

	/**
	 * @param eemSubscriptionId the eemSubscriptionId to set
	 */
	public void setEemSubscriptionId(String eemSubscriptionId) {
		this.eemSubscriptionId = eemSubscriptionId;
	}

	/**
	 * @return the testCaseResult
	 */
	public Map<String, ExecutionResult> getTestCaseResult() {
		return testCaseResult;
	}

	/**
	 * @param testCaseResult the testCaseResult to set
	 */
	public void setTestCaseResult(Map<String, ExecutionResult> testCaseResult) {
		this.testCaseResult = testCaseResult;
	}

	/**
	 * @return the reportUrl
	 */
	public String getReportUrl() {
		return reportUrl;
	}

	/**
	 * @param reportUrl the reportUrl to set
	 */
	public void setReportUrl(String reportUrl) {
		this.reportUrl = reportUrl;
	}

	/**
	 * @return the executionId
	 */
	public String getExecutionId() {
		return executionId;
	}

	/**
	 * @return the testCaseDescriptorConfiguration
	 */
	public List<TestCaseExecutionConfiguration> getTestCaseDescriptorConfiguration() {
		return testCaseDescriptorConfiguration;
	}

	
	
}
