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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import it.nextworks.nfvmano.elm.im.ExperimentExecution;
import it.nextworks.nfvmano.elm.sbi.eem.ExperimentExecutionSubscriptionRequest.SubscriptionTypeEnum;
import it.nextworks.nfvmano.elm.sbi.eem.interfaces.EemConsumerInterface;
import it.nextworks.nfvmano.elm.sbi.eem.messages.RunExecutionRequest;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MalformattedElementException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MethodNotImplementedException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.NotExistingEntityException;

public class EemDriver extends EemAbstractDriver {

	private static final Logger log = LoggerFactory.getLogger(EemDriver.class);
	
	private String notificationUrl;
	
	private RestTemplate restTemplate;
	
	public EemDriver(String eemAddress, int eemPort, String noficationUrl) {
		super(EemDriverType.EEM, null);
		this.notificationUrl = noficationUrl;
		this.eemUrl = "http://" + eemAddress + ":" + eemPort + "/eem/";
		this.restTemplate= new RestTemplate(new BufferingClientHttpRequestFactory(
				new SimpleClientHttpRequestFactory()
		));
	}

	@Override
	public String createExperimentExecutionInstance() throws MethodNotImplementedException, FailedOperationException {
		log.debug("Sending HTTP request to create a new experiment execution");
		
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "application/json");
		
		HttpEntity<?> postEntity = new HttpEntity<>(header);
		
		String url = eemUrl + "/experiment_executions";
		try {
			ResponseEntity<String> httpResponse =
					restTemplate.exchange(url, HttpMethod.POST, postEntity, String.class);
			
			log.debug("Response code: " + httpResponse.getStatusCode().toString());
			HttpStatus code = httpResponse.getStatusCode();
			
			if (code.equals(HttpStatus.CREATED)) {
				log.debug("Experiment execution correctly created");

				String experimentExecutionId = httpResponse.getBody();
                log.debug("Experiment execution ID: " + experimentExecutionId);

				return experimentExecutionId;
			} else {
				throw new FailedOperationException("Generic error during ELM interaction with EEM");
			}
			
		} catch (Exception e) {
			log.debug("Error while interacting with EEM.");
			throw new FailedOperationException("Error while interacting with EEM: " + e.getMessage());
		}	
	}

	@Override
	public void runExperimentExecution(RunExecutionRequest request)
			throws MethodNotImplementedException, FailedOperationException, NotExistingEntityException, MalformattedElementException {
		log.debug("Sending HTTP request to run an experiment execution");
		
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "application/json");
		
		HttpEntity<?> postEntity = new HttpEntity<>(request, header);
		String url = eemUrl + "/experiment_executions/" + request.getExecutionId() + "/run";

		try {
			ResponseEntity<String> httpResponse =
					restTemplate.exchange(url, HttpMethod.POST, postEntity, String.class);
			
			log.debug("Response code: " + httpResponse.getStatusCode().toString());
			HttpStatus code = httpResponse.getStatusCode();
			
			if (code.equals(HttpStatus.OK)) {
				log.debug("Experiment execution run request correctly sent");

				String message = httpResponse.getBody();
                log.debug("Returned message: " + message);
			} else {
				throw new FailedOperationException("Generic error during ELM interaction with EEM");
			}
			
		} catch (Exception e) {
			log.debug("Error while interacting with EEM.");
			throw new FailedOperationException("Error while interacting with EEM: " + e.getMessage());
		}
	}

	@Override
	public ExperimentExecution getExperimentExecution(String experimentExecutionId)
			throws MethodNotImplementedException, FailedOperationException, NotExistingEntityException {
		log.debug("Sending HTTP request for querying Experiment execution with ID " + experimentExecutionId);
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "application/json");
		HttpEntity<?> getEntity = new HttpEntity<>(header);
		String url = eemUrl + "/experiment_executions/" + experimentExecutionId;
		
		try {
			ResponseEntity<ExperimentExecution> httpResponse =
					restTemplate.exchange(url, HttpMethod.GET, getEntity, ExperimentExecution.class);
			
			log.debug("Response code: " + httpResponse.getStatusCode().toString());
			HttpStatus code = httpResponse.getStatusCode();
			
			if (code.equals(HttpStatus.OK)) {
				log.debug("Experiment execution correctly retrieved");

				return httpResponse.getBody();
			} else {
				throw new FailedOperationException("Generic error during ELM interaction with EEM");
			}
			
		} catch (Exception e) {
			log.debug("Error while interacting with EEM.");
			throw new FailedOperationException("Error while interacting with EEM: " + e.getMessage());
		}
	}

	@Override
	public void abortExperimentExecution(String experimentExecutionId)
			throws MethodNotImplementedException, FailedOperationException, NotExistingEntityException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeExperimentExecutionRecord(String experimentExecutionId)
			throws MethodNotImplementedException, FailedOperationException, NotExistingEntityException {
		log.debug("Sending HTTP request for deleting record of experiment execution with ID " + experimentExecutionId);
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "application/json");
		HttpEntity<?> deleteEntity = new HttpEntity<>(header);
		String url = eemUrl + "/experiment_executions/" + experimentExecutionId;

		try {
			ResponseEntity<?> httpResponse =
					restTemplate.exchange(url, HttpMethod.DELETE, deleteEntity, String.class);
			
			log.debug("Response code: " + httpResponse.getStatusCode().toString());
			HttpStatus code = httpResponse.getStatusCode();
			
			if (code.equals(HttpStatus.NO_CONTENT)) {
				log.debug("Experiment execution record correctly removed");
			} else {
				throw new FailedOperationException("Generic error during ELM interaction with EEM");
			}
			
		} catch (Exception e) {
			log.debug("Error while interacting with EEM.");
			throw new FailedOperationException("Error while interacting with EEM: " + e.getMessage());
		}
		
	}

	@Override
	public String subscribe(String experimentExecutionId, EemConsumerInterface consumer)
			throws MethodNotImplementedException, FailedOperationException, NotExistingEntityException {
		log.debug("Sending HTTP request to subscribe for experiment execution " + experimentExecutionId);
		
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "application/json");
		
		ExperimentExecutionSubscriptionRequest request = new ExperimentExecutionSubscriptionRequest(SubscriptionTypeEnum.STATE, notificationUrl, experimentExecutionId);
		
		HttpEntity<?> postEntity = new HttpEntity<>(request, header);
		String url = eemUrl + "/experiment_subscriptions";

		try {
			ResponseEntity<String> httpResponse =
					restTemplate.exchange(url, HttpMethod.POST, postEntity, String.class);
			
			log.debug("Response code: " + httpResponse.getStatusCode().toString());
			HttpStatus code = httpResponse.getStatusCode();
			
			if (code.equals(HttpStatus.CREATED)) {
				log.debug("Experiment execution subscription correctly created");

				String response = httpResponse.getBody();
				String subscriptionID = response;
                log.debug("Subscription ID: " + subscriptionID);
                return subscriptionID;
			} else {
				throw new FailedOperationException("Generic error during ELM interaction with EEM");
			}
			
		} catch (Exception e) {
			log.debug("Error while interacting with EEM.");
			throw new FailedOperationException("Error while interacting with EEM: " + e.getMessage());
		}
	}

	@Override
	public void unsubscribe(String subscriptionId)
			throws MethodNotImplementedException, FailedOperationException, NotExistingEntityException {
		log.debug("Sending HTTP request for deleting experiment execution subscription with ID " + subscriptionId);
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "application/json");
		HttpEntity<?> deleteEntity = new HttpEntity<>(header);
		String url = eemUrl + "/experiment_subscriptions/" + subscriptionId;

		try {
			ResponseEntity<?> httpResponse =
					restTemplate.exchange(url, HttpMethod.DELETE, deleteEntity, String.class);
			
			log.debug("Response code: " + httpResponse.getStatusCode().toString());
			HttpStatus code = httpResponse.getStatusCode();
			
			if (code.equals(HttpStatus.NO_CONTENT)) {
				log.debug("Experiment execution record correctly removed");
			} else {
				throw new FailedOperationException("Generic error during ELM interaction with EEM");
			}
			
		} catch (Exception e) {
			log.debug("Error while interacting with EEM.");
			throw new FailedOperationException("Error while interacting with EEM: " + e.getMessage());
		}

	}

}
