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

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import it.nextworks.nfvmano.elm.engine.ExperimentLifecycleManagerEngine;
import it.nextworks.nfvmano.elm.im.ExperimentExecution;
import it.nextworks.nfvmano.elm.sbi.eem.interfaces.EemConsumerInterface;
import it.nextworks.nfvmano.elm.sbi.eem.interfaces.EemProviderInterface;
import it.nextworks.nfvmano.elm.sbi.eem.messages.RunExecutionRequest;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MalformattedElementException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MethodNotImplementedException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.NotExistingEntityException;

@Service
public class EemService implements EemProviderInterface {
	
	private static final Logger log = LoggerFactory.getLogger(EemService.class);

	private EemProviderInterface driver;
	
	@Value("${eem.address}")
	private String eemAddress;
	
	@Value("${eem.port}")
	private String eemPort;
	
	@Value("${eem.notification.url}")
	private String eemNotificationUrl;

	@Value("${eem.type}")
	private String eemType;
	
	@Value("${eem.fakeReportUrl}")
	private String fakeReportUrl;
	
	@Value("${eem.dummysuccessfulprob}")
	private double succProb;
	
	@Autowired
	private ExperimentLifecycleManagerEngine engine;
	
	public EemService() { }
	
	@PostConstruct
	public void init() {
		log.debug("Initializing EEM driver");
		if (eemType.equals("DUMMY")) {
			this.driver = new DummyEemDriver(fakeReportUrl, succProb);
		} else if (eemType.equals("EEM")) {
			this.driver = new EemDriver(eemAddress, Integer.parseInt(eemPort), eemNotificationUrl);
		} else {
			log.error("Wrong configuration for EEM service.");
		}
	}

	@Override
	public String createExperimentExecutionInstance() throws MethodNotImplementedException, FailedOperationException {
		return driver.createExperimentExecutionInstance();
	}
	
	@Override
	public void runExperimentExecution(RunExecutionRequest request) throws MethodNotImplementedException, FailedOperationException, NotExistingEntityException, MalformattedElementException {
		driver.runExperimentExecution(request);
	}
	
	@Override
	public ExperimentExecution getExperimentExecution(String experimentExecutionId) throws MethodNotImplementedException, FailedOperationException, NotExistingEntityException {
		return driver.getExperimentExecution(experimentExecutionId);
	}
	
	@Override
	public void abortExperimentExecution(String experimentExecutionId) throws MethodNotImplementedException, FailedOperationException, NotExistingEntityException {
		driver.abortExperimentExecution(experimentExecutionId);
	}
	
	@Override
	public void removeExperimentExecutionRecord(String experimentExecutionId) throws MethodNotImplementedException, FailedOperationException, NotExistingEntityException {
		driver.removeExperimentExecutionRecord(experimentExecutionId);
	}
	
	@Override
	public String subscribe(String experimentExecutionId, EemConsumerInterface consumer) throws MethodNotImplementedException, FailedOperationException, NotExistingEntityException {
		return driver.subscribe(experimentExecutionId, engine);
	}
	
	@Override
	public void unsubscribe(String subscriptionId) throws MethodNotImplementedException, FailedOperationException, NotExistingEntityException {
		driver.unsubscribe(subscriptionId);
	}
	
}
