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
package it.nextworks.nfvmano.elm.sbi.eem.interfaces;

import it.nextworks.nfvmano.elm.im.ExperimentExecution;
import it.nextworks.nfvmano.elm.sbi.eem.messages.RunExecutionRequest;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MalformattedElementException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MethodNotImplementedException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.NotExistingEntityException;

public interface EemProviderInterface {

	/**
	 * This method creates a new experiment execution instance and returns its ID
	 * 
	 * @return the ID of the experiment execution instance
	 * @throws MethodNotImplementedException if the method is not implemented
	 * @throws FailedOperationException if the operation fails
	 */
	public String createExperimentExecutionInstance() throws MethodNotImplementedException, FailedOperationException;
	
	/**
	 * This method requests to run a given execution
	 * 
	 * @param request parameters to run the experiment
	 * @throws MethodNotImplementedException if the method is not implemented
	 * @throws FailedOperationException if the operation fails
	 * @throws NotExistingEntityException if the execution does not exist
	 * @throws MalformattedElementException if the request is malformatted
	 */
	public void runExperimentExecution(RunExecutionRequest request) throws MethodNotImplementedException, FailedOperationException, NotExistingEntityException, MalformattedElementException;
	
	/**
	 * This method returns the most updated information about an experiment execution, including its results when the execution is terminated
	 * 
	 * @param experimentExecutionId ID of the experiment execution
	 * @return the details of the requested execution
	 * @throws MethodNotImplementedException if the method is not implemented
	 * @throws FailedOperationException if the operation fails
	 * @throws NotExistingEntityException if the requested experiment execution does not exist
	 */
	public ExperimentExecution getExperimentExecution(String experimentExecutionId) throws MethodNotImplementedException, FailedOperationException, NotExistingEntityException;
	
	/**
	 * This method requests to abort a running experiment execution
	 * 
	 * @param experimentExecutionId ID of the experiment execution to abort
	 * @throws MethodNotImplementedException if the method is not implemented
	 * @throws FailedOperationException if the operation fails
	 * @throws NotExistingEntityException if the requested experiment execution does not exist
	 */
	public void abortExperimentExecution(String experimentExecutionId) throws MethodNotImplementedException, FailedOperationException, NotExistingEntityException;
	
	/**
	 * This method removes the record of a given experiment execution
	 * 
	 * @param experimentExecutionId ID of the experiment execution to be removed
	 * @throws MethodNotImplementedException if the method is not implemented
	 * @throws FailedOperationException if the operation fails
	 * @throws NotExistingEntityException if the experiment execution does not exist
	 */
	public void removeExperimentExecutionRecord(String experimentExecutionId) throws MethodNotImplementedException, FailedOperationException, NotExistingEntityException;
	
	/**
	 * This method requests the subscription to the events associated to the change of status for a given execution
	 * 
	 * @param experimentExecutionId ID of the target execution
	 * @param consumer entity to be notified
	 * @return the ID of the subscription
	 * @throws MethodNotImplementedException if the method is not implemented
	 * @throws FailedOperationException if the operation fails
	 * @throws NotExistingEntityException if the experiment execution does not exist
	 */
	public String subscribe(String experimentExecutionId, EemConsumerInterface consumer) throws MethodNotImplementedException, FailedOperationException, NotExistingEntityException;
	
	/**
	 * This method requests to remove a given subscription
	 * 
	 * @param subscriptionId ID of the subscription to be removed
	 * @throws MethodNotImplementedException if the method is not implemented
	 * @throws FailedOperationException if the operation fails
	 * @throws NotExistingEntityException if the subscription does not exist
	 */
	public void unsubscribe(String subscriptionId) throws MethodNotImplementedException, FailedOperationException, NotExistingEntityException;
	
}
