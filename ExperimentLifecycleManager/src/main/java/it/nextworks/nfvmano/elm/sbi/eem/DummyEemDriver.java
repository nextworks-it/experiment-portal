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
package it.nextworks.nfvmano.elm.sbi.eem;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.nextworks.nfvmano.elm.im.ExecutionResult;
import it.nextworks.nfvmano.elm.im.ExperimentExecution;
import it.nextworks.nfvmano.elm.im.ExperimentExecutionStatus;
import it.nextworks.nfvmano.elm.sbi.eem.interfaces.EemConsumerInterface;
import it.nextworks.nfvmano.elm.sbi.eem.messages.ExperimentExecutionStatusChangeNotification;
import it.nextworks.nfvmano.elm.sbi.eem.messages.RunExecutionRequest;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MalformattedElementException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MethodNotImplementedException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.NotExistingEntityException;

public class DummyEemDriver extends EemAbstractDriver {
	
	private static final Logger log = LoggerFactory.getLogger(DummyEemDriver.class);
	
	private Map<String, ExperimentExecution> execs = new HashMap<String, ExperimentExecution>();
	
	private Map<String, EemConsumerInterface> consumers = new HashMap<>();
	
	private Map<String, String> subscriptions = new HashMap<>();
	
	public DummyEemDriver() {
		super(EemDriverType.DUMMY, null);
	}

	@Override
	public String createExperimentExecutionInstance() throws MethodNotImplementedException, FailedOperationException {
		log.debug("Received request to create a new experiment execution");
		UUID execIdUuid = UUID.randomUUID();
		String execId = execIdUuid.toString();
		log.debug("Created new execution ID " + execId);
		ExperimentExecution exec = new ExperimentExecution(null, execId, null);
		execs.put(execId, exec);
		log.debug("Exec " + execId + " stored internally");
		return execId;
	}
	
	@Override
	public void runExperimentExecution(RunExecutionRequest request) throws MethodNotImplementedException, FailedOperationException, NotExistingEntityException, MalformattedElementException {
		request.isValid();
		String execId = request.getExecutionId();
		log.debug("Received request to run experiment execution " + execId);
		if (execs.containsKey(execId)) {
			ExperimentExecution exec = execs.get(execId);
			exec.setStatus(ExperimentExecutionStatus.TERMINATED);
			exec.setReportUrl("Fake URL");
			Map<String, ExecutionResult> results = new HashMap<>();
			results.put("TCD_ID", new ExecutionResult("Successful"));
			exec.setTestCaseResult(results);
			execs.put(execId, exec);
			
			EemConsumerInterface consumer = consumers.get(execId);
			consumer.notifyExperimentExecutionStatusChange(new ExperimentExecutionStatusChangeNotification(execId, ExperimentExecutionStatus.CONFIGURING));
			consumer.notifyExperimentExecutionStatusChange(new ExperimentExecutionStatusChangeNotification(execId, ExperimentExecutionStatus.RUNNING));
			consumer.notifyExperimentExecutionStatusChange(new ExperimentExecutionStatusChangeNotification(execId, ExperimentExecutionStatus.TERMINATED));
		} else throw new NotExistingEntityException("Execution " + execId + " not found");
	}
	
	@Override
	public ExperimentExecution getExperimentExecution(String experimentExecutionId) throws MethodNotImplementedException, FailedOperationException, NotExistingEntityException {
		log.debug("Received query about execution with ID " + experimentExecutionId);
		if (execs.containsKey(experimentExecutionId)) {
			return execs.get(experimentExecutionId);
		} else throw new NotExistingEntityException("Execution " + experimentExecutionId + " not found");
	}
	
	@Override
	public void abortExperimentExecution(String experimentExecutionId) throws MethodNotImplementedException, FailedOperationException, NotExistingEntityException {
		throw new MethodNotImplementedException();
	}
	
	@Override
	public void removeExperimentExecutionRecord(String experimentExecutionId) throws MethodNotImplementedException, FailedOperationException, NotExistingEntityException {
		log.debug("Received request to remove experiment execution " + experimentExecutionId);
		if (execs.containsKey(experimentExecutionId)) {
			execs.remove(experimentExecutionId);
			log.debug("Execution " + experimentExecutionId + " removed.");
		} else throw new NotExistingEntityException("Execution " + experimentExecutionId + " not found");
	}
	
	@Override
	public String subscribe(String experimentExecutionId, EemConsumerInterface consumer) throws MethodNotImplementedException, FailedOperationException, NotExistingEntityException {
		log.debug("Received request to subscribe for experiment execution " + experimentExecutionId);
		if (execs.containsKey(experimentExecutionId)) {
			UUID subIdUuid = UUID.randomUUID();
			String subId = subIdUuid.toString();
			log.debug("Created new subscription ID " + subId);
			subscriptions.put(subId, experimentExecutionId);
			consumers.put(experimentExecutionId, consumer);
			log.debug("Stored subscription info");
			return subId;
		} else throw new NotExistingEntityException("Execution " + experimentExecutionId + " not found");
	}
	
	@Override
	public void unsubscribe(String subscriptionId) throws MethodNotImplementedException, FailedOperationException, NotExistingEntityException {
		log.debug("Received request to unsubscribe for subscription " + subscriptionId);
		if (subscriptions.containsKey(subscriptionId)) {
			String execId = subscriptions.get(subscriptionId);
			consumers.remove(execId);
			subscriptions.remove(subscriptionId);
			log.debug("Removed subscription " + subscriptionId);
		} else throw new NotExistingEntityException("Subscription " + subscriptionId + " not found.");
	}

	
}
