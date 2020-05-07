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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.nextworks.nfvmano.elm.im.Experiment;
import it.nextworks.nfvmano.elm.sbi.ticketing.TicketOperationException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.NotExistingEntityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.nextworks.nfvmano.catalogue.blueprint.elements.ApplicationMetric;
import it.nextworks.nfvmano.catalogue.blueprint.elements.CtxBlueprint;
import it.nextworks.nfvmano.catalogue.blueprint.elements.CtxDescriptor;
import it.nextworks.nfvmano.catalogue.blueprint.elements.EveSite;
import it.nextworks.nfvmano.catalogue.blueprint.elements.ExpBlueprint;
import it.nextworks.nfvmano.catalogue.blueprint.elements.ExpDescriptor;
import it.nextworks.nfvmano.catalogue.blueprint.elements.InfrastructureMetric;
import it.nextworks.nfvmano.catalogue.blueprint.elements.KeyPerformanceIndicator;
import it.nextworks.nfvmano.catalogue.blueprint.elements.TestCaseBlueprint;
import it.nextworks.nfvmano.catalogue.blueprint.elements.TestCaseDescriptor;
import it.nextworks.nfvmano.catalogue.blueprint.elements.VsBlueprint;
import it.nextworks.nfvmano.catalogue.blueprint.elements.VsDescriptor;
import it.nextworks.nfvmano.catalogue.blueprint.messages.QueryCtxBlueprintResponse;
import it.nextworks.nfvmano.catalogue.blueprint.messages.QueryCtxDescriptorResponse;
import it.nextworks.nfvmano.catalogue.blueprint.messages.QueryExpBlueprintResponse;
import it.nextworks.nfvmano.catalogue.blueprint.messages.QueryTestCaseBlueprintResponse;
import it.nextworks.nfvmano.catalogue.blueprint.messages.QueryTestCaseDescriptorResponse;
import it.nextworks.nfvmano.catalogue.blueprint.messages.QueryVsBlueprintResponse;
import it.nextworks.nfvmano.catalogue.blueprint.messages.QueryVsDescriptorResponse;
import it.nextworks.nfvmano.catalogue.translator.NfvNsInstantiationInfo;
import it.nextworks.nfvmano.elm.engine.messages.DeployExperimentInternalMessage;
import it.nextworks.nfvmano.elm.engine.messages.EemNotificationInternalMessage;
import it.nextworks.nfvmano.elm.engine.messages.ExecuteExperimentInternalMessage;
import it.nextworks.nfvmano.elm.engine.messages.InternalMessage;
import it.nextworks.nfvmano.elm.engine.messages.InternalMessageType;
import it.nextworks.nfvmano.elm.engine.messages.NotifyNfvNsStatusChangeInternalMessage;
import it.nextworks.nfvmano.elm.engine.messages.ScheduleExperimentInternalMessage;
import it.nextworks.nfvmano.elm.engine.messages.TerminateExperimentInternalMessage;
import it.nextworks.nfvmano.elm.engine.messages.UpdateExperimentStateInternalMessage;
import it.nextworks.nfvmano.elm.im.ExperimentExecution;
import it.nextworks.nfvmano.elm.im.ExperimentExecutionStatus;
import it.nextworks.nfvmano.elm.im.ExperimentStatus;
import it.nextworks.nfvmano.elm.sbi.eem.EemService;
import it.nextworks.nfvmano.elm.sbi.eem.messages.RunExecutionRequest;
import it.nextworks.nfvmano.elm.sbi.expcatalogue.SbiExperimentCatalogueService;
import it.nextworks.nfvmano.elm.sbi.monitoring.DataCollectionManagerDriver;
import it.nextworks.nfvmano.elm.sbi.monitoring.MonitoringDataItem;
import it.nextworks.nfvmano.elm.sbi.monitoring.MonitoringDataType;
import it.nextworks.nfvmano.elm.sbi.ticketing.LcTicketUpdateType;
import it.nextworks.nfvmano.elm.sbi.ticketing.TicketingSystemService;
import it.nextworks.nfvmano.libs.ifa.common.elements.Filter;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;
import it.nextworks.nfvmano.libs.ifa.common.messages.GeneralizedQueryRequest;
import it.nextworks.nfvmano.libs.ifa.common.messages.SubscribeRequest;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.NsDf;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.NsLevel;
import it.nextworks.nfvmano.libs.ifa.descriptors.nsd.Nsd;
import it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.messages.CreateNsIdentifierRequest;
import it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.messages.InstantiateNsRequest;
import it.nextworks.nfvmano.libs.ifa.osmanfvo.nslcm.interfaces.messages.TerminateNsRequest;
import it.nextworks.nfvmano.nfvodriver.NfvoLcmService;
import it.nextworks.nfvmano.nfvodriver.NsStatusChange;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import javax.annotation.PostConstruct;

public class ExperimentInstanceManager {

	private static final Logger log = LoggerFactory.getLogger(ExperimentInstanceManager.class);
	
	private String experimentId; 
	private ExpDescriptor experimentDescriptor;
	private ExperimentStatus status;
	//private String lcTicketId;
	private List<EveSite> targetSites = new ArrayList<EveSite>();
	private String tenantId;
	private String nsInstanceId;
	private String msnoSubscriptionId;
	private String currentExecutionId;
	private String eemSubscriptionId;
	
	private ExperimentLifecycleManagerEngine engine;
	private ExperimentRecordsManager experimentRecordManager;
	private TicketingSystemService ticketingSystemService;
	private SbiExperimentCatalogueService sbiExperimentCatalogueService;
	private NfvoLcmService nfvoLcmService;
	private DataCollectionManagerDriver dcmDriver;
	private EemService eemService;
	
	private ExpBlueprint experimentBlueprint;
	private VsBlueprint vsBlueprint;
	private List<CtxBlueprint> ctxBlueprints = new ArrayList<>();
	private List<TestCaseBlueprint> tcBlueprints = new ArrayList<>();
	
	private VsDescriptor vsDescriptor;
	private List<CtxDescriptor> ctxDescriptors = new ArrayList<>();
	private List<TestCaseDescriptor> tcDescriptors = new ArrayList<>();

	
	//Used for monitoring
	List<MonitoringDataItem> applicationMonitoringMetrics = new ArrayList<>();
	List<MonitoringDataItem> infrastructureMonitoringMetrics = new ArrayList<>();
	List<MonitoringDataItem> monitoringKpis = new ArrayList<>();

    private Map<String, String> ticketingAddresses;


    private List<String> openTicketIds = new ArrayList<>();
	
	public ExperimentInstanceManager(String experimentId,
									 ExpDescriptor experimentDescriptor,
									 String tenantId,
									 List<EveSite> targetSites,
									 ExperimentLifecycleManagerEngine engine,
									 ExperimentRecordsManager experimentRecordManager,
									 TicketingSystemService ticketingSystemService,
									 SbiExperimentCatalogueService sbiExperimentCatalogueService,
									 NfvoLcmService nfvoLcmService,
									 DataCollectionManagerDriver dcmDriver,
									 EemService eemService,
									 boolean loadFromCatalogue,
                                     Map<String, String> ticketingAddresses) {
		this.experimentId = experimentId;
		this.experimentDescriptor = experimentDescriptor;
		this.tenantId = tenantId;
		this.status = ExperimentStatus.SCHEDULING;
		if (targetSites != null) this.targetSites = targetSites;
		this.engine = engine;
		this.experimentRecordManager = experimentRecordManager;
		this.ticketingSystemService = ticketingSystemService;
		this.msnoSubscriptionId=null;
		this.eemSubscriptionId = null;

		this.sbiExperimentCatalogueService = sbiExperimentCatalogueService;
		this.nfvoLcmService = nfvoLcmService;
		this.dcmDriver = dcmDriver;
		this.eemService = eemService;
		this.ticketingAddresses=ticketingAddresses;
		try {
    		 if(loadFromCatalogue) loadInformationFromPortalCatalogue();
    	} catch (Exception e) {
    		log.error("Error while retrieving information from portal catalogue: " + e.getMessage());
    		manageExpError("Error while retrieving information from portal catalogue: " + e.getMessage());
    	}
	}
	
	public ExperimentInstanceManager(String experimentId,
			ExpDescriptor experimentDescriptor,
			String tenantId,
			List<EveSite> targetSites,
			ExperimentStatus status,
			List<String> openTicketIds,
			String nsInstanceId,
			String currentExecutionId,
			String eemSubscriptionId,
			String msnoSubscriptionId,
			ExperimentLifecycleManagerEngine engine,
			ExperimentRecordsManager experimentRecordManager,
			TicketingSystemService ticketingSystemService,
			SbiExperimentCatalogueService sbiExperimentCatalogueService,
			NfvoLcmService nfvoLcmService,
			DataCollectionManagerDriver dcmDriver,
			EemService eemService,
									 boolean loadFromCatalogue,
                                     Map<String,String> ticketingAddresses) {
		this.experimentId = experimentId;
		this.experimentDescriptor = experimentDescriptor;
		this.tenantId = tenantId;
		this.status = status;
		if (targetSites != null) this.targetSites = targetSites;
		this.nsInstanceId = nsInstanceId;
		this.eemSubscriptionId = eemSubscriptionId;
		this.msnoSubscriptionId = msnoSubscriptionId;
		this.currentExecutionId = currentExecutionId;
		this.engine = engine;
		this.experimentRecordManager = experimentRecordManager;
		this.ticketingSystemService = ticketingSystemService;
		this.openTicketIds = openTicketIds;
		this.sbiExperimentCatalogueService = sbiExperimentCatalogueService;
		this.nfvoLcmService = nfvoLcmService;
		this.dcmDriver = dcmDriver;
		this.eemService = eemService;
		this.ticketingAddresses=ticketingAddresses;
		try {
			if(loadFromCatalogue) loadInformationFromPortalCatalogue();
    	} catch (Exception e) {
    		log.error("Error while retrieving information from portal catalogue: " + e.getMessage());
    		manageExpError("Error while retrieving information from portal catalogue: " + e.getMessage());
    	}
	}

	/**
     * Method used to receive messages about experiment LCM from the Rabbit MQ
     *
     * @param message received message
     */
    public void receiveMessage(String message) {
        log.debug("Received message for Experiment " + experimentId + "\n" + message);
        
        try {
            ObjectMapper mapper = Jackson2ObjectMapperBuilder.json()
					.modules(new JavaTimeModule())
					.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
					.build();
            InternalMessage im = mapper.readValue(message, InternalMessage.class);
            InternalMessageType imt = im.getType();
            
            switch (imt) {
            	case SCHEDULE_REQUEST: {
            		log.debug("Processing request to schedule the new experiment " + experimentId);
            		ScheduleExperimentInternalMessage msg = (ScheduleExperimentInternalMessage) im;
            		processSchedulingRequest(msg);
            		break;
            	}
            	
            	case UPDATE_STATE_REQUEST: {
            		log.debug("Processing request to change the status of the experiment " + experimentId);
            		UpdateExperimentStateInternalMessage msg = (UpdateExperimentStateInternalMessage) im;
            		processStatusChangeRequest(msg);
            		break;
            	}
            	
            	case DEPLOY_EXPERIMENT_REQUEST: {
            		log.debug("Processing request to deploy the experiment " + experimentId);
            		DeployExperimentInternalMessage msg = (DeployExperimentInternalMessage) im;
            		processDeployRequest(msg);
            		break;
            	}
            	
            	case NFV_NS_STATUS_CHANGE_NOTIFICATION: {
            		log.debug("Processing NFV NS change status notification for experiment " + experimentId);
            		NotifyNfvNsStatusChangeInternalMessage msg = (NotifyNfvNsStatusChangeInternalMessage) im;
            		processNfvoNotification(msg);
            		break;
            	}
            	
            	case EXECUTE_EXPERIMENT_REQUEST: {
            		log.debug("Processing execution request for experiment " + experimentId);
            		ExecuteExperimentInternalMessage msg = (ExecuteExperimentInternalMessage) im;
            		processExecutionRequest(msg);
            		break;
            	}
            	
            	case EEM_NOTIFICATION: {
            		log.debug("Processing EEM  notification for experiment " + experimentId);
            		EemNotificationInternalMessage msg = (EemNotificationInternalMessage) im;
            		processEemNotification(msg);
            		break;
            	}
            	
            	case TERMINATE_EXPERIMENT_REQUEST: {
            		log.debug("Processing termination request for experiment " + experimentId);
            		TerminateExperimentInternalMessage msg = (TerminateExperimentInternalMessage) im;
            		processTerminateRequest(msg);
            		break;
            	}
            	
            	default:
                    log.error("Received message with not supported type. Skipping.");
                    break;
            }
        
        } catch (JsonParseException e) {
            manageExpError("Error while parsing message: " + e.getMessage());
        } catch (JsonMappingException e) {
            manageExpError("Error in Json mapping: " + e.getMessage());
        } catch (IOException e) {
            manageExpError("IO error when receiving json message: " + e.getMessage());
        }

    }
    
    public ExperimentStatus getExperimentStatus() {
    	return status; 
    }
    
    /**
	 * @return the tenantId
	 */
	public String getTenantId() {
		return tenantId;
	}
	
	/**
	 * @return the experimentId
	 */
	public String getExperimentId() {
		return experimentId;
	}

	private void processSchedulingRequest(ScheduleExperimentInternalMessage msg) {
    	log.debug("Internal processing request to schedule a new experiment");
    	if (status != ExperimentStatus.SCHEDULING) {
    		log.debug("Scheduling request for experiment " + experimentId + " in wrong status. Skipping.");
    		return;
    	}
		try {
			Experiment experiment=experimentRecordManager.retrieveExperimentFromId(experimentId);
			for(EveSite site : experiment.getTargetSites()){
                if(ticketingAddresses.containsKey(site.toString())){
                    String siteAdminAddress = ticketingAddresses.get(site.toString());
                    String ticketId = ticketingSystemService.createSchedulingTicket(experiment, experimentDescriptor, msg.getRequest().getProposedTimeSlot(),
                            site.toString(), siteAdminAddress,msg.getRequester());
                    experiment.addTicket(ticketId);

                }else{
                    log.error("Site admin address not configured, ignoring ticket creation");
                }

            }
			log.debug("Experiment: "+experimentId+" saving new list of open ticket:");
			this.openTicketIds=experiment.getOpenTicketIds();
			experimentRecordManager.updateExperimentOpenTickets(experimentId, experiment.getOpenTicketIds());

		} catch (NotExistingEntityException e) {
			log.error("Error retrieving experiment, this should not happen:", e);
		} catch (TicketOperationException e) {
			log.error("Error creating experiment thicket" , e);
		}

	}
    
    private void processStatusChangeRequest(UpdateExperimentStateInternalMessage msg) {
    	log.debug("Internal processing request to change the status of experiment " + experimentId);
    	//here we assume all the consistency checks have been performed at the caller
    	status = msg.getRequest().getStatus();
    	experimentRecordManager.setExperimentStatus(experimentId, msg.getRequest().getStatus());
    	log.debug("Set new status in DB");
		try {
			for(String ticketId : openTicketIds){
				log.debug("Updating ticket: "+ticketId);
				ticketingSystemService.updateSchedulingTicket(ticketId, getLcTicketTypeFromTargetState(msg.getRequest().getStatus()), msg.getRequester());
			}

		} catch (TicketOperationException e) {
			log.error("Failed to update ticket status:", e);
		}
	}
    
    private static String getNsDescriptorId(String nsdIdentifier, String dfId, String ilId){
        String seed = nsdIdentifier + "_" + dfId + "_" + ilId;
        return UUID.nameUUIDFromBytes(seed.getBytes()).toString();
    }
    
    private void processDeployRequest(DeployExperimentInternalMessage msg) {
    	log.debug("Internal processing request to deploy experiment " + experimentId);
    	experimentRecordManager.setExperimentStatus(experimentId, ExperimentStatus.INSTANTIATING);
    	status = ExperimentStatus.INSTANTIATING;
    	log.debug("Translating experiment descriptor into NFV NS info.");
    	try {
    		NfvNsInstantiationInfo nsInfo = sbiExperimentCatalogueService.translateExpd(experimentDescriptor.getExpDescriptorId());
    		List<String> domains = new ArrayList<>();
    		for (EveSite es : targetSites) domains.add(es.toString());
    		nsInfo.setDomainIds(domains);
    		log.debug("Creating NFV NS instance ID.");
    		String remoteNsdId = getNsDescriptorId(nsInfo.getNfvNsdId(), nsInfo.getDeploymentFlavourId(), nsInfo.getInstantiationLevelId());
    		//String remoteNsdId = nsInfo.getNfvNsdId() + "_" + nsInfo.getDeploymentFlavourId() + "_" + nsInfo.getInstantiationLevelId();		//this depends on how it is mapped in the NFVO Catalogue driver
    		
    		String nsInstanceId = nfvoLcmService.createNsIdentifier(new CreateNsIdentifierRequest(remoteNsdId, 
    				"NS_exp_" + experimentId, 
    				"NFV NS instance for experiment " + experimentId, 
    				tenantId));
    		
    		log.debug("Created NS " + nsInstanceId + " for experiment " + experimentId);
    		this.nsInstanceId = nsInstanceId;
    		experimentRecordManager.setExperimentNfvNsId(experimentId, nsInstanceId);
    		
    		/*
    		log.debug("Subscribing to MSNO for receiving notifications related to NS instance " + nsInstanceId);
    		Map<String, String> parameters = new HashMap<>();
			parameters.put("NS_ID", nsInstanceId);
			Filter filter = new Filter(parameters);
			SubscribeRequest subscribeRequest = new SubscribeRequest(filter, null);	//the callback is filled automatically by the driver
			String subscriptionId = nfvoLcmService.subscribeNsLcmEvents(subscribeRequest, null);		//mgt of subscriptions via REST controller still to be implemented
			this.msnoSubscriptionId = subscriptionId;

			log.debug("Created subscription with ID " + subscriptionId);
			*/
    		
    		experimentRecordManager.setExperimentCurrentMsnoSubscriptionId(experimentId, this.msnoSubscriptionId);
    		
    		Map<String, String> additionalParamForNs = new HashMap<String, String>();
    		String dst = "";
    		//for (EveSite es : targetSites) dst += es.toString() + ";";
    		//one single site at the moment
    		dst = targetSites.get(0).toString();
    		additionalParamForNs.put("target-site", dst);
    		InstantiateNsRequest instantiateRequest = new InstantiateNsRequest(nsInstanceId, 
    				nsInfo.getDeploymentFlavourId(), 
    				null, 
    				null, 
    				null, 
    				null, 
    				null, 
    				additionalParamForNs, 
    				null, 
    				null, 
    				nsInfo.getInstantiationLevelId(), 
    				null);
    		
    		nfvoLcmService.instantiateNs(instantiateRequest);
    		log.debug("Sent request for instantiating NS " + nsInstanceId + " associated to experiment " + experimentId);
    		
    		//TODO: add starting of thread to automate the deletion of the NS instance at the experiment expiration time
    		
    	} catch (Exception e) {
    		log.error("Failure while processing deployment request: " + e.getMessage());
    		manageExpError("Failure while processing deployment request: " + e.getMessage());
    	}
    }
    
    private void processNfvoNotification(NotifyNfvNsStatusChangeInternalMessage msg) {
    	log.debug("Internal processing of NFVO notification related to experiment " + experimentId);
    	NsStatusChange statusChange = msg.getStatusChange();
    	
    	switch (statusChange) {
     	case NS_CREATED: {
     		processNfvNsInstantiationNotification(msg.isSuccessful());
     		break;
     	}
     	
     	case NS_TERMINATED: {
     		processNfvNsTerminationNotification(msg.isSuccessful());
     		break;
     	}
     	 	
     	default:
             log.error("Received NFVO notification with not supported type. Skipping.");
             break;
     }
    	
    }
    
    private void processExecutionRequest(ExecuteExperimentInternalMessage msg) {
    	//here we assume the check on the status has been already performed by the caller
    	log.debug("Internal processing of experiment execution request");
    	experimentRecordManager.setExperimentStatus(experimentId, ExperimentStatus.RUNNING_EXECUTION);
    	status = ExperimentStatus.RUNNING_EXECUTION;
    	try {
    		currentExecutionId = eemService.createExperimentExecutionInstance();
    		String executionName = msg.getRequest().getExecutionName();
    		log.debug("Created experiment execution " + executionName + " with ID " + currentExecutionId + " for experiment " + experimentId);
    		eemSubscriptionId = eemService.subscribe(currentExecutionId, engine);
    		experimentRecordManager.setExperimentCurrentEemSubscriptionId(experimentId, eemSubscriptionId);
    		log.debug("Created subscription " + eemSubscriptionId + " for execution " + currentExecutionId);
    		List<String> siteNames = new ArrayList<>();
    		siteNames.add(targetSites.get(0).toString());
    		experimentRecordManager.createExperimentExecution(experimentId, 
    				currentExecutionId,
    				executionName,
    				msg.getRequest().getTestCaseDescriptorConfiguration(), 
    				eemSubscriptionId);
    		Experiment experiment = experimentRecordManager.retrieveExperimentFromId(experimentId);
    		eemService.runExperimentExecution(new RunExecutionRequest(
    												currentExecutionId,
													experimentDescriptor.getExpDescriptorId(),
													msg.getRequest().getTestCaseDescriptorConfiguration(),
													nsInstanceId,
													tenantId,
													siteNames,
													experimentId,
													experiment.getUseCase()
					));
    		log.debug("Requested execution run to EEM");
    	} catch (Exception e) {
    		log.error("Error while requesting the experiment execution: " + e.getMessage());
    	}
    }
    
    private void processEemNotification(EemNotificationInternalMessage msg) {
    	//here we assume the check on the status has been already performed by the caller
    	log.debug("Internal processing of EEM notification");
    	String executionId = msg.getNotification().getExperimentExecutionId();
    	if (!currentExecutionId.equals(executionId)) {
    		log.error("Received notification related to an execution ID different from the current one. Skipping.");
    		return;
    	}
    	ExperimentExecutionStatus execStatus = msg.getNotification().getCurrentStatus();
    	switch (execStatus) {
		case CONFIGURING:
		case RUNNING:
		case VALIDATING: {
			log.debug("The execution is in status " + execStatus.toString());
			experimentRecordManager.updateExperimentExecutionStatus(executionId, execStatus, new HashMap<>(), null);
			log.debug("Internal database updated.");
			break;
		}
		
		case COMPLETED:
		case FAILED: {
			log.debug("The execution is in status " + execStatus.toString());
			status = ExperimentStatus.INSTANTIATED;
			experimentRecordManager.setExperimentStatus(experimentId, ExperimentStatus.INSTANTIATED);
			try {
				ExperimentExecution execution = eemService.getExperimentExecution(executionId);
				experimentRecordManager.updateExperimentExecutionStatus(executionId, 
						ExperimentExecutionStatus.COMPLETED, 
						execution.getTestCaseResult(), 
						execution.getReportUrl());
				eemService.unsubscribe(eemSubscriptionId);
				//TODO: shall we remove from EEM?
				eemService.removeExperimentExecutionRecord(executionId);
				eemSubscriptionId = null;
				currentExecutionId = null;
			} catch (Exception e) {
				log.error("Error while interacting with EEM to retrieve execution information.");
			}
			break;
		}
		
		default: {
			log.error("Unexpected status in EEM notification. Skipping message.");
			break;
		}
		}
    }
    
    private void processTerminateRequest(TerminateExperimentInternalMessage msg) {
    	//here we assume the check on the status has been already performed by the caller
    	log.debug("Internal processing of termination request.");
    	experimentRecordManager.setExperimentStatus(experimentId, ExperimentStatus.TERMINATING);
    	status = ExperimentStatus.TERMINATING;
    	try {
    		unsubscribeFromMonitoring();
    	} catch (Exception e) {
    		log.error("Failure while unsubscribing monitoring for experiment " + experimentId + ". Proceeding with NFV NS removal.");
    	}
    	try {
    		nfvoLcmService.terminateNs(new TerminateNsRequest(nsInstanceId, null));
    		log.debug("Sent request for terminating NS " + nsInstanceId + " associated to experiment " + experimentId);
    	} catch (Exception e) {
    		log.error("Failure while processing termination request: " + e.getMessage() + ". Forcing the experiment in terminated state.");
    		status = ExperimentStatus.TERMINATED;
    		experimentRecordManager.setExperimentStatus(experimentId, ExperimentStatus.TERMINATED);
    	}
    }
    
    private void processNfvNsInstantiationNotification(boolean successful) {
    	if (status != ExperimentStatus.INSTANTIATING) {
    		log.debug("NFVO NS instantiation result notification for experiment " + experimentId + " in wrong status. Skipping.");
    		return;
    	}
    	if (successful) {
    		log.debug("Experiment " + experimentId + " correctly instantiated.");
    		status = ExperimentStatus.INSTANTIATED;
    		experimentRecordManager.setExperimentStatus(experimentId, ExperimentStatus.INSTANTIATED);
    		log.debug("Set new status in DB");
    		try {
    			subscribeForMonitoring();
    		} catch (FailedOperationException e) {

    			manageExpError("Failed to subscribe for automated experiment monitoring. Setting status to failed");
				manageExpError(e.getMessage());
				status = ExperimentStatus.FAILED;
				experimentRecordManager.setExperimentStatus(experimentId, ExperimentStatus.FAILED);
			}
    	} else {
    		manageExpError("Failed instantiation of NFV NS for experiment " + experimentId);
    		status = ExperimentStatus.FAILED;
    		experimentRecordManager.setExperimentStatus(experimentId, ExperimentStatus.FAILED);
    	}
    }
    
    private void processNfvNsTerminationNotification(boolean successful) {
    	if (status != ExperimentStatus.TERMINATING) {
    		log.debug("NFVO NS termination result notification for experiment " + experimentId + " in wrong status. Skipping.");
    		return;
    	}
    	if (successful) {
    		log.debug("Experiment " + experimentId + " correctly terminated.");
    		status = ExperimentStatus.TERMINATED;
    		experimentRecordManager.setExperimentStatus(experimentId, ExperimentStatus.TERMINATED);
    		log.debug("Set new status in DB");
    		try {
    			//log.debug("Unsubscribing from MSNO for NS istance " + nsInstanceId);
    			//nfvoLcmService.unsubscribeNsLcmEvents(msnoSubscriptionId);
    			//log.debug("Unsubscribed from MSNO");
				log.debug("Deleting ns identifier {}", nsInstanceId);
				nfvoLcmService.deleteNsIdentifier(nsInstanceId);
				log.debug("Deleted ns identifier {}", nsInstanceId);
    		} catch (Exception e) {
    			log.error("Got an exception when unsubscribing from MSNO: " + e.getMessage());
    		}
    	} else {
    		manageExpError("Failed termination of NFV NS for experiment " + experimentId);
    	}
    }
    
    private void subscribeForMonitoring() throws FailedOperationException {
    	log.debug("Retrieving metrics for monitoring subscription");
    	List<InfrastructureMetric> expbMetrics = experimentBlueprint.getMetrics();
    	List<ApplicationMetric> applicationMetrics = vsBlueprint.getApplicationMetrics();
		Experiment experiment = null;
    	try {
    		experiment=experimentRecordManager.retrieveExperimentFromId(experimentId);
		} catch (NotExistingEntityException e) {
			log.error("Error retrieving experiment: "+experimentId, e);
			throw  new FailedOperationException(e.getMessage());
		}
    	String useCase = experiment.getUseCase();
		for (CtxBlueprint cb : ctxBlueprints) {
    		applicationMetrics.addAll(cb.getApplicationMetrics());
    	}
    	//at the moment we manage a single site
    	EveSite site = targetSites.get(0);
    	for (InfrastructureMetric im : expbMetrics) {
    		log.debug("Infrastructure metric: " + " ID: " + im.getMetricId() + " Name: " + im.getName());
    		MonitoringDataItem dataItem = new MonitoringDataItem(experimentId, MonitoringDataType.INFRASTRUCTURE_METRIC, site, im.getMetricId(),
                    im.getName(), im.getMetricGraphType(), im.getMetricCollectionType(), im.getUnit(), im.getInterval(), useCase);
    		infrastructureMonitoringMetrics.add(dataItem);
            log.debug("adding application metric: "+im.getMetricId());

    	}
    	for (ApplicationMetric am : applicationMetrics) {
    		log.debug("Application metric: " + " ID: " + am.getMetricId() + " Name: " + am.getName());
            MonitoringDataItem dataItem = new MonitoringDataItem(experimentId, MonitoringDataType.APPLICATION_METRIC, site, am.getMetricId(),
                    am.getName(), am.getMetricGraphType(), am.getMetricCollectionType(), am.getUnit(), am.getInterval(), useCase);
    		applicationMonitoringMetrics.add(dataItem);
            log.debug("adding application metric: "+am.getMetricId());
    	}
    	dcmDriver.subscribe(infrastructureMonitoringMetrics, MonitoringDataType.INFRASTRUCTURE_METRIC);
    	dcmDriver.subscribe(applicationMonitoringMetrics, MonitoringDataType.APPLICATION_METRIC);
    	log.debug("Subscribed for monitoring metrics");
    	
    	log.debug("Retrieving KPIs for monitoring subscription");
    	List<KeyPerformanceIndicator> kpis = experimentBlueprint.getKpis();
    	for (KeyPerformanceIndicator kpi : kpis) {
    	    MonitoringDataItem dataItem = new MonitoringDataItem(experimentId, MonitoringDataType.KPI, site, kpi.getKpiId(),
                    kpi.getName(), kpi.getKpiGraphType(), null , kpi.getUnit(), kpi.getInterval(), useCase );
    		monitoringKpis.add(dataItem);
    	}
    	dcmDriver.subscribe(monitoringKpis, MonitoringDataType.KPI);
    	log.debug("Subscribed for monitoring KPIs");
    	
    	log.debug("Retrieving results for monitoring subscription - not supported at the moment");
    	//TODO: Add subscription for experiment results when available
    }
    
    private void unsubscribeFromMonitoring() throws FailedOperationException {
    	log.debug("Removing monitoring subscriptions for experiment " + experimentId);
    	dcmDriver.unsubscribe(infrastructureMonitoringMetrics, MonitoringDataType.INFRASTRUCTURE_METRIC);
    	dcmDriver.unsubscribe(applicationMonitoringMetrics, MonitoringDataType.APPLICATION_METRIC);
    	dcmDriver.unsubscribe(monitoringKpis, MonitoringDataType.KPI);
    	log.debug("Monitoring subscriptions removed for experiment " + experimentId);
    }
    
    private void loadInformationFromPortalCatalogue() throws Exception {
    	log.debug("Loading information from portal catalogue about descriptors and blueprints");
    	
    	String experimentBlueprintId = experimentDescriptor.getExpBlueprintId();
    	QueryExpBlueprintResponse expB = sbiExperimentCatalogueService.queryExpBlueprint(buildQueryForParameter("ExpB_ID", experimentBlueprintId));
    	experimentBlueprint = expB.getExpBlueprintInfo().get(0).getExpBlueprint();
    	
    	String vsBlueprintId = experimentBlueprint.getVsBlueprintId();
    	QueryVsBlueprintResponse vsb = sbiExperimentCatalogueService.queryVsBlueprint(buildQueryForParameter("VSB_ID", vsBlueprintId));
    	vsBlueprint = vsb.getVsBlueprintInfo().get(0).getVsBlueprint();
		
    	List<String> ctxBlueprintIds = experimentBlueprint.getCtxBlueprintIds();
    	for (String id : ctxBlueprintIds) {
    		QueryCtxBlueprintResponse ctxB = sbiExperimentCatalogueService.queryCtxBlueprint(buildQueryForParameter("CTXB_ID", id));
    		ctxBlueprints.add(ctxB.getCtxBlueprintInfos().get(0).getCtxBlueprint());
    	}
    	
    	List<String> tcBlueprintIds = experimentBlueprint.getTcBlueprintIds();
    	for (String id : tcBlueprintIds) {
    		QueryTestCaseBlueprintResponse tcB = sbiExperimentCatalogueService.queryTestCaseBlueprint(buildQueryForParameter("TCB_ID", id));
    		tcBlueprints.add(tcB.getTestCaseBlueprints().get(0).getTestCaseBlueprint());
    	}
    	
    	String vsdId = experimentDescriptor.getVsDescriptorId();
    	QueryVsDescriptorResponse vsd = sbiExperimentCatalogueService.queryVsDescriptor(buildQueryForParameter("VSD_ID", vsdId));
    	vsDescriptor = vsd.getVsDescriptors().get(0);
    	
    	List<String> ctxDescriptorIds = experimentDescriptor.getCtxDescriptorIds();
    	for (String id : ctxDescriptorIds) {
    		QueryCtxDescriptorResponse ctxD = sbiExperimentCatalogueService.queryCtxDescriptor(buildQueryForParameter("CTXD_ID", id));
    		ctxDescriptors.add(ctxD.getCtxDescriptors().get(0));
    	}
    	
    	List<String> testCaseDescriptorIds = experimentDescriptor.getTestCaseDescriptorIds();
    	for (String id : testCaseDescriptorIds) {
    		QueryTestCaseDescriptorResponse tcD = sbiExperimentCatalogueService.queryTestCaseDescriptor(buildQueryForParameter("TCD_ID", id));
    		tcDescriptors.add(tcD.getTestCaseDescriptors().get(0));
    	}
    	
    	log.debug("Loaded all the blueprints and descriptor information from portal catalogue");
    }

    public void loadInformationFromPortalCatalogue(ExpDescriptor experimentDescriptor) throws Exception {
		this.experimentDescriptor = experimentDescriptor;
		loadInformationFromPortalCatalogue();
	}
    
    private GeneralizedQueryRequest buildQueryForParameter(String parameterName, String parameterValue) {
    	Map<String,String> parameters = new HashMap<>();
		parameters.put(parameterName, parameterValue);
		Filter filter = new Filter(parameters);
		return new GeneralizedQueryRequest(filter, null);
    }


	public void setExperimentDescriptor(ExpDescriptor experimentDescriptor) {
		this.experimentDescriptor = experimentDescriptor;
	}

	private void manageExpError(String errorMessage) {
        log.error(errorMessage);
        this.status = ExperimentStatus.FAILED;
        experimentRecordManager.setExperimentError(experimentId, errorMessage);
    }
	
    private LcTicketUpdateType getLcTicketTypeFromTargetState(ExperimentStatus targetStatus) {
    	switch (targetStatus) {
		case ACCEPTED:
			return LcTicketUpdateType.NOTIFY_ACCEPTANCE;
			
		case REFUSED:
			return LcTicketUpdateType.NOTIFY_REFUSAL;
		
		case READY:
			return LcTicketUpdateType.NOTIFY_ENV_READY;
			
		default: {
			log.error("Unexpected target status");
			return null;
		}
		}
    }



    
}
