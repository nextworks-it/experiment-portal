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
package it.nextworks.nfvmano.elm.engine;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import it.nextworks.nfvmano.elm.im.*;
import it.nextworks.nfvmano.elm.repos.ExperimentSapInfoRepository;
import it.nextworks.nfvmano.elm.sbi.monitoring.MonitoringDataItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import it.nextworks.nfvmano.catalogue.blueprint.elements.EveSite;
import it.nextworks.nfvmano.elm.repos.ExperimentExecutionRepository;
import it.nextworks.nfvmano.elm.repos.ExperimentRepository;
import it.nextworks.nfvmano.elm.repos.TestCaseExecutionConfigurationRepository;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MalformattedElementException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.NotExistingEntityException;

@Service
public class ExperimentRecordsManager {

	private static final Logger log = LoggerFactory.getLogger(ExperimentRecordsManager.class);
	
	@Value("${elm.admin}")
	private String adminTenant;

	@Autowired
	private ExperimentSapInfoRepository experimentSapInfoRepository;
	
	@Autowired
	private ExperimentRepository experimentRepository;
	
	@Autowired
	private ExperimentExecutionRepository experimentExecutionRepository;
	
	@Autowired TestCaseExecutionConfigurationRepository testCaseExecutionConfigurationRepository;
	
	public ExperimentRecordsManager() {	}
	
	public synchronized String createExperiment(String experimentDescriptorId, String experimentName, String tenantId, ExperimentExecutionTimeslot timeslot, List<EveSite> targetSites, String useCase
												//,Map<String, EveSite> perServiceSite
	) {
		log.debug("Storing a new experiment instance in DB.");
		Experiment experiment = new Experiment(experimentDescriptorId, experimentName, tenantId, timeslot, targetSites, useCase
				//, perServiceSite
				);
		experimentRepository.saveAndFlush(experiment);
		UUID id = experiment.getId();
		String experimentId = id.toString();
		experiment.setExperimentId(experimentId);
		experimentRepository.saveAndFlush(experiment);
		log.debug("Stored new experiment instance with ID " + experimentId);
		return experimentId;
	}
	
	public synchronized void createExperimentExecution(String experimentId, 
			String executionId,
			String executionName,
			Map<String, Map<String, String>> testCaseDescriptorConfiguration,
			String eemSubscriptionId) throws NotExistingEntityException {
		log.debug("Storing a new experiment execution in DB.");
		Experiment experiment = retrieveExperimentFromId(experimentId);
		ExperimentExecution experimentExecution = new ExperimentExecution(experiment, executionId, executionName, eemSubscriptionId);
		experimentExecutionRepository.saveAndFlush(experimentExecution);
		
		for (Map.Entry<String, Map<String, String>> e : testCaseDescriptorConfiguration.entrySet()) {
			String tcdId = e.getKey();
			Map<String, String> configParam = e.getValue();
			TestCaseExecutionConfiguration config = new TestCaseExecutionConfiguration(experimentExecution, tcdId, configParam);
			testCaseExecutionConfigurationRepository.saveAndFlush(config);
		}
		
		experiment.setCurrentExecutionId(executionId);
		experimentRepository.saveAndFlush(experiment);
		log.debug("Stored experiment execution with ID " + executionId + " for experiment " + experimentId);
	}
	
	public synchronized void setExperimentError(String experimentId, String errorMessage) {
		log.debug("Setting failure for experiment " + experimentId);
		try {
			Experiment experiment = retrieveExperimentFromId(experimentId);
			experiment.setErrorMessage(errorMessage);
			experiment.setStatus(ExperimentStatus.FAILED);
			experimentRepository.saveAndFlush(experiment);
		} catch (NotExistingEntityException e) {
			log.error("Impossible to set the experiment " + experimentId + " to failed since it is not found in the db.");
		}
	}
	
	public synchronized void updateExperimentOpenTickets(String experimentId, List<String> openTicketIds) {
		log.debug("Setting LC ticket ID for experiment " + experimentId);
		try {
			Experiment experiment = retrieveExperimentFromId(experimentId);
			experiment.setOpenTicketIds(openTicketIds);
			experimentRepository.saveAndFlush(experiment);
		} catch (NotExistingEntityException e) {
			log.error("Impossible to set LC ticket ID for the experiment " + experimentId + " since it is not found in the db.");
		}
	}
	
	public synchronized void setExperimentStatus(String experimentId, ExperimentStatus status) {
		log.debug("Setting status " + status.toString() + " for experiment " + experimentId);
		try {
			Experiment experiment = retrieveExperimentFromId(experimentId);
			experiment.setStatus(status);
			experimentRepository.saveAndFlush(experiment);
		} catch (NotExistingEntityException e) {
			log.error("Impossible to set status for the experiment " + experimentId + " since it is not found in the db.");
		}
	}

	public synchronized void setExperimentCurrentEemSubscriptionId(String experimentId, String currentEemSubscriptionId) {
		log.debug("Setting CurrentEemSubscriptionId " + currentEemSubscriptionId + " for experiment " + experimentId);
		try {
			Experiment experiment = retrieveExperimentFromId(experimentId);
			experiment.setCurrentEemSubscriptionId(currentEemSubscriptionId);
			experimentRepository.saveAndFlush(experiment);
		} catch (NotExistingEntityException e) {
			log.error("Impossible to set currentEemSusbcriptionId for the experiment " + experimentId + " since it is not found in the db.");
		}
	}

	public synchronized void setExperimentCurrentMsnoSubscriptionId(String experimentId, String currentMsnoSubscriptionId) {
		log.debug("Setting CurrentMsnoSubscriptionId " + currentMsnoSubscriptionId + " for experiment " + experimentId);
		try {
			Experiment experiment = retrieveExperimentFromId(experimentId);
			experiment.setCurrentMsnoSubscriptionId(currentMsnoSubscriptionId);
			experimentRepository.saveAndFlush(experiment);
		} catch (NotExistingEntityException e) {
			log.error("Impossible to set currentMsnoSusbcriptionId for the experiment " + experimentId + " since it is not found in the db.");
		}
	}
	
	public synchronized void setExperimentNfvNsId(String experimentId, String nfvNsId) {
		log.debug("Setting NFV NS instance ID " + nfvNsId + " for experiment " + experimentId);
		try {
			Experiment experiment = retrieveExperimentFromId(experimentId);
			experiment.setNfvNsInstanceId(nfvNsId);
			experimentRepository.saveAndFlush(experiment);
		} catch (NotExistingEntityException e) {
			log.error("Impossible to set NFV NS instance ID for the experiment " + experimentId + " since it is not found in the db.");
		}
	}

	public synchronized  void updateExperimentSapInfo(String experimentId, List<ExperimentSapInfo> sapInfos){
		try {
			Experiment experiment = retrieveExperimentFromId(experimentId);
			for(ExperimentSapInfo sapInfo: sapInfos){
				sapInfo.setExperiment(experiment);
				experimentSapInfoRepository.saveAndFlush(sapInfo);
			}
			experiment.setSapInfo(sapInfos);
			experimentRepository.saveAndFlush(experiment);
		} catch (NotExistingEntityException e) {
			log.error("imposible to update experiment sap info");
		}


	}

	
	public synchronized void updateExperimentExecutionStatus(String executionId, ExperimentExecutionStatus status, Map<String, ExecutionResult> testCaseResult, String reportUrl) {
		log.debug("Updating result for experiment execution " + executionId);
		try {
			ExperimentExecution ee = retrieveExperimentExecution(executionId);
			ee.setStatus(status);
			ee.setReportUrl(reportUrl);
			ee.setTestCaseResult(testCaseResult);
			experimentExecutionRepository.saveAndFlush(ee);
		} catch (Exception e) {
			log.error("Impossible to update experiment execution results in DB: " + e.getMessage());
		}
	}

	public synchronized void updateExperimentInfrastructureMetrics(String experimentId, List<MonitoringDataItem> metrics){
		log.debug("Updating experiment infrastructure metrics");
		try {
			Experiment experiment = retrieveExperimentFromId(experimentId);
			experiment.setInfrastructureMetrics(metrics);
			experimentRepository.saveAndFlush(experiment);
		} catch (NotExistingEntityException e) {
			log.error("Unable to retrieve experiment information",e);
		}
	}


	public synchronized void updateExperimentApplicationMetrics(String experimentId, List<MonitoringDataItem> metrics){
		log.debug("Updating experiment infrastructure metrics");
		try {
			Experiment experiment = retrieveExperimentFromId(experimentId);
			experiment.setApplicationMetrics(metrics);
			experimentRepository.saveAndFlush(experiment);
		} catch (NotExistingEntityException e) {
			log.error("Unable to retrieve experiment information",e);
		}
	}

	public synchronized  void updateExperimentMonitoringKpis(String experimentId, List<MonitoringDataItem> metrics){
		log.debug("Updating experiment infrastructure metrics");
		try {
			Experiment experiment = retrieveExperimentFromId(experimentId);
			experiment.setMonitoringKpis(metrics);
			experimentRepository.saveAndFlush(experiment);
		} catch (NotExistingEntityException e) {
			log.error("Unable to retrieve experiment information",e);
		}
	}
	
	public synchronized void removeExperiment(String experimentId) {
		log.debug("Removing experiment " + experimentId + " from DB.");
		try {
			Experiment experiment = retrieveExperimentFromId(experimentId);
			experimentRepository.delete(experiment);
		} catch (Exception e) {
			log.error("Impossible to remove experiment " + experimentId + " from DB.");
		}
	}
	
	//GETs
	
	public Experiment retrieveExperimentFromId(String experimentId) throws NotExistingEntityException {
		log.debug("Retrieving experiment with ID " + experimentId);
		Optional<Experiment> expOpt = experimentRepository.findByExperimentId(experimentId);
		if (expOpt.isPresent()) return expOpt.get();
		else throw new NotExistingEntityException("Experiment with ID " + experimentId + " not present in DB.");
	}
	
	public Experiment retrieveExperimentFromIdAndTenant(String experimentId, String tenantId) throws NotExistingEntityException, MalformattedElementException {
		log.debug("Retrieving experiment with ID " + experimentId + " associated to tenant " + tenantId);
		if (tenantId == null) throw new MalformattedElementException("Tenant ID is null");
		if (experimentId == null) throw new MalformattedElementException("Experiment ID is null");
		if (tenantId.equals(adminTenant)) {
			log.debug("Admin tenant: the experiment will be returned independently on its own tenant");
			return retrieveExperimentFromId(experimentId);
		} else {
				Optional<Experiment> expOpt = experimentRepository.findByTenantIdAndExperimentId(tenantId, experimentId);
				if (expOpt.isPresent()) return expOpt.get();
				else throw new NotExistingEntityException("Experiment with ID " + experimentId + " not present in DB for tenant " + tenantId);
		}
	}
	
	public Experiment retrieveExperimentFromNsId(String nsId) throws NotExistingEntityException, MalformattedElementException {
		log.debug("Retrieving experiment associated to NS instance with ID " + nsId);
		if (nsId == null) throw new MalformattedElementException("NS ID is null");
		Optional<Experiment> expOpt = experimentRepository.findByNfvNsInstanceId(nsId);
		if (expOpt.isPresent()) return expOpt.get();
		else throw new NotExistingEntityException("Experiment associated to NS instance with ID " + nsId + " not present in DB.");
	}
	
	public List<Experiment> retrieveAllExperiments(String tenantId) throws MalformattedElementException {
		log.debug("Retrieving all experiments for tenant " + tenantId);
		if (tenantId == null) throw new MalformattedElementException("Tenant ID is null");
		if (tenantId.equals(adminTenant)) {
			log.debug("Admin tenant: all experiments will be returned");
			return experimentRepository.findAll();
		} else {
			return experimentRepository.findByTenantId(tenantId);
		}
	}
	
	public List<Experiment> retrieveExperimentsFromExpDescriptorId(String experimentDescriptorId, String tenantId) throws MalformattedElementException {
		log.debug("Retrieving experiments with descriptor ID " + experimentDescriptorId + " associated to tenant " + tenantId);
		if (tenantId == null) throw new MalformattedElementException("Tenant ID is null");
		if (experimentDescriptorId == null) throw new MalformattedElementException("Experiment descriptor ID is null");
		if (tenantId.equals(adminTenant)) {
			log.debug("Admin tenant: the experiments will be returned independently on their own tenant");
			return experimentRepository.findByExperimentDescriptorId(experimentDescriptorId);
		} else {
			return experimentRepository.findByTenantIdAndExperimentDescriptorId(tenantId, experimentDescriptorId);
		}
	}
	
	public ExperimentExecution retrieveExperimentExecution(String experimentExecutionId) throws NotExistingEntityException, MalformattedElementException {
		log.debug("Retrieving experiment execution with ID " + experimentExecutionId);
		if (experimentExecutionId == null) throw new MalformattedElementException("Execution ID is null");
		Optional<ExperimentExecution> execOpt = experimentExecutionRepository.findByExecutionId(experimentExecutionId);
		if (execOpt.isPresent()) return execOpt.get();
		else throw new NotExistingEntityException("Experiment execution with ID " + experimentExecutionId + " not present in DB.");
	}

	public List<Experiment> retrieveAllActiveExperiments(){
		log.debug("Retrieving all active experiments");
		List<Experiment> allExperiments = experimentRepository.findAll();
		List<Experiment> activeExperiments = allExperiments.stream()
				.filter(currentExperiment -> !currentExperiment.getStatus().equals(ExperimentStatus.TERMINATED))
				.collect(Collectors.toList());
		return activeExperiments;
	}

    public List<Experiment> retrieveExperimentFromSite(EveSite site) {

		log.debug("Retrieving all active experiments");
		List<Experiment> allExperiments = experimentRepository.findAll();
		List<Experiment> siteExperiments = allExperiments.stream()
				.filter(currentExperiment -> currentExperiment.getTargetSites().contains(site))
				.collect(Collectors.toList());
		return siteExperiments;
    }
}
