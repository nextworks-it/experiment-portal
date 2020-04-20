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
package it.nextworks.nfvmano.elm.nbi;

import java.util.List;

import it.nextworks.nfvmano.elm.im.Experiment;
import it.nextworks.nfvmano.elm.nbi.messages.ExecuteExperimentRequest;
import it.nextworks.nfvmano.elm.nbi.messages.ExperimentSchedulingRequest;
import it.nextworks.nfvmano.elm.nbi.messages.ModifyExperimentTimeslotRequest;
import it.nextworks.nfvmano.elm.nbi.messages.UpdateExperimentStatusRequest;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MalformattedElementException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MethodNotImplementedException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.NotExistingEntityException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.NotPermittedOperationException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.WrongStatusException;
import it.nextworks.nfvmano.libs.ifa.common.messages.GeneralizedQueryRequest;

public interface ExperimentLifecycleManagerProviderInterface {

	/**
	 * Method to request the scheduling for a new experiment
	 * 
	 * @param request request with the details of the experiment and the proposed timeslot
	 * @param tenantId ID of the tenant requesting the service
	 * @param tenantEmail email of the tenant requesting the service
	 * @return the ID of the experiment instance
	 * @throws NotExistingEntityException if the experiment descriptor does not exist
	 * @throws MalformattedElementException if the request is malformed
	 * @throws FailedOperationException if the operation fails
	 * @throws MethodNotImplementedException if the method is not implemented
	 */
	public String scheduleNewExperiment(ExperimentSchedulingRequest request, String tenantId, String tenantEmail)
			throws NotExistingEntityException, MalformattedElementException, FailedOperationException, MethodNotImplementedException; 

	/**
	 * Method to retrieve the list of experiments matching a given filter
	 * 
	 * @param request query request
	 * @return the list of experiments matching the filter specified in the request
	 * @throws MalformattedElementException if the request is malformed
	 * @throws FailedOperationException if the operation fails
	 * @throws MethodNotImplementedException if the method is not implemented
	 * @throws NotExistingEntityException if the requested experiment is not found
	 */
	public List<Experiment> getExperiments(GeneralizedQueryRequest request)
			throws MalformattedElementException, FailedOperationException, MethodNotImplementedException, NotExistingEntityException;
	
	/**
	 * Method to update the status of an existing experiment.
	 * Manual change of status can be done only between:
	 * - scheduling and accepted
	 * - scheduling and refused
	 * - accepted and ready 
	 * 
	 * @param request request with the new status
	 * @param tenantId ID of the tenant requesting the service modification
	 * @throws NotExistingEntityException if the experiment does not exist
	 * @throws MalformattedElementException if the request is malformed
	 * @throws FailedOperationException if the operation fails
	 * @throws WrongStatusException if the experiment is not in scheduling or accepted state
	 * @throws NotPermittedOperationException if the change between the two requested states cannot be performed according to the experiment state machine
	 * @throws MethodNotImplementedException if the method is not implemented
	 */
	public void updateExperimentStatus(UpdateExperimentStatusRequest request, String tenantId)
			throws NotExistingEntityException, MalformattedElementException, FailedOperationException, WrongStatusException, NotPermittedOperationException, MethodNotImplementedException;
	
	/**
	 * Method to change the proposed timeslot for an existing experiment.
	 * The experiment must be in scheduling state.
	 * 
	 * @param request request with the new proposed timeslot
	 * @param tenantId ID of the tenant requesting the service modification
	 * @throws NotExistingEntityException if the experiment does not exist
	 * @throws MalformattedElementException if the request is malformed
	 * @throws FailedOperationException if the operation fails
	 * @throws WrongStatusException if the experiment is not in scheduling state
	 * @throws MethodNotImplementedException if the method is not implemented
	 */
	public void updateExperimentTimeslot(ModifyExperimentTimeslotRequest request, String tenantId)
			throws NotExistingEntityException, MalformattedElementException, FailedOperationException, WrongStatusException, MethodNotImplementedException;
	
	/**
	 * Method to start the deployment of an existing experiment.
	 * The experiment must be in ready state.
	 * 
	 * 
	 * @param experimentId ID of the experiment to be deployed
	 * @param tenantId ID of the tenant requesting the service deployment
	 * @throws NotExistingEntityException if the experiment does not exist
	 * @throws MalformattedElementException if the request is malformed
	 * @throws FailedOperationException if the operation fails
	 * @throws WrongStatusException if the experiment is not in ready state
	 * @throws NotPermittedOperationException if the current time is not included in the agreed timeslot
	 * @throws MethodNotImplementedException if the method is not implemented
	 */
	public void deployExperiment(String experimentId, String tenantId) 
			throws NotExistingEntityException, MalformattedElementException, FailedOperationException, WrongStatusException, NotPermittedOperationException, MethodNotImplementedException;
	
	/**
	 * Method to execute an existing experiment.
	 * The experiment must be in instantiated state.
	 * 
	 * 
	 * @param request request with the specification of the experiment execution
	 * @param tenantId ID of the tenant requesting the service deployment
	 * @throws NotExistingEntityException if the experiment does not exist
	 * @throws MalformattedElementException if the request is malformed
	 * @throws FailedOperationException if the operation fails
	 * @throws WrongStatusException if the experiment is not in instantiated state
	 * @throws NotPermittedOperationException if the current time is not included in the agreed timeslot
	 * @throws MethodNotImplementedException if the method is not implemented
	 */
	public void executeExperiment(ExecuteExperimentRequest request, String tenantId) 
			throws NotExistingEntityException, MalformattedElementException, FailedOperationException, WrongStatusException, NotPermittedOperationException, MethodNotImplementedException;
	
	/**
	 * Method to terminate an existing experiment, i.e. removing its network services.
	 * The experiment must be in instantiated state.
	 * 
	 * @param experimentId ID of the experiment to be terminated
	 * @param tenantId ID of the tenant requesting the service termination
	 * @throws NotExistingEntityException if the experiment does not exist
	 * @throws MalformattedElementException if the request is malformed
	 * @throws FailedOperationException if the operation fails
	 * @throws WrongStatusException if the experiment is not in instantiated state
	 * @throws NotPermittedOperationException if the tenant is not allowed to terminate the experiment
	 * @throws MethodNotImplementedException if the method is not implemented
	 */
	public void terminateExperiment(String experimentId, String tenantId)
			throws NotExistingEntityException, MalformattedElementException, FailedOperationException, WrongStatusException, NotPermittedOperationException, MethodNotImplementedException;
	
	/**
	 * Method to remove an existing experiment.
	 * The experiment must be in terminated or failed or refused state.
	 * 
	 * @param experimentId ID of the experiment to be removed
	 * @param tenantId ID of the tenant requesting the experiment removal
	 * @throws NotExistingEntityException if the experiment does not exist
	 * @throws MalformattedElementException if the request is malformed
	 * @throws FailedOperationException if the operation fails
	 * @throws WrongStatusException if the experiment is not in terminated, failed or refused state
	 * @throws NotPermittedOperationException if the tenant is not allowed to remove the experiment
	 * @throws MethodNotImplementedException if the method is not implemented
	 */
	public void purgeExperiment(String experimentId, String tenantId)
			throws NotExistingEntityException, MalformattedElementException, FailedOperationException, WrongStatusException, NotPermittedOperationException, MethodNotImplementedException;
	
	/**
	 * Method to abort an existing experiment.
	 * The experiment cannot be in terminated status.
	 * 
	 * @param experimentId ID of the experiment to be aborted
	 * @param tenantId ID of the tenant requesting the service abortion
	 * @throws NotExistingEntityException if the experiment does not exist
	 * @throws MalformattedElementException if the request is malformed
	 * @throws FailedOperationException if the operation fails
	 * @throws WrongStatusException if the experiment is in terminated status
	 * @throws MethodNotImplementedException if the method is not implemented
	 */
	public void abortExperiment(String experimentId, String tenantId)
			throws NotExistingEntityException, MalformattedElementException, FailedOperationException, WrongStatusException, MethodNotImplementedException;
}
