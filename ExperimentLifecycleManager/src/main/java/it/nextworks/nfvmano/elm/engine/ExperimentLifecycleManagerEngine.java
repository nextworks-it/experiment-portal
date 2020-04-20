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

import java.time.OffsetDateTime;
import java.util.*;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.nextworks.nfvmano.elm.im.ExperimentExecutionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import it.nextworks.nfvmano.catalogue.blueprint.elements.EveSite;
import it.nextworks.nfvmano.catalogue.blueprint.elements.ExpDescriptor;
import it.nextworks.nfvmano.catalogue.blueprint.messages.QueryExpDescriptorResponse;
import it.nextworks.nfvmano.elm.ConfigurationParameters;
import it.nextworks.nfvmano.elm.engine.messages.DeployExperimentInternalMessage;
import it.nextworks.nfvmano.elm.engine.messages.EemNotificationInternalMessage;
import it.nextworks.nfvmano.elm.engine.messages.ExecuteExperimentInternalMessage;
import it.nextworks.nfvmano.elm.engine.messages.InternalMessage;
import it.nextworks.nfvmano.elm.engine.messages.NotifyNfvNsStatusChangeInternalMessage;
import it.nextworks.nfvmano.elm.engine.messages.ScheduleExperimentInternalMessage;
import it.nextworks.nfvmano.elm.engine.messages.TerminateExperimentInternalMessage;
import it.nextworks.nfvmano.elm.engine.messages.UpdateExperimentStateInternalMessage;
import it.nextworks.nfvmano.elm.im.Experiment;
import it.nextworks.nfvmano.elm.im.ExperimentExecution;
import it.nextworks.nfvmano.elm.im.ExperimentStatus;
import it.nextworks.nfvmano.elm.nbi.ExperimentLifecycleManagerProviderInterface;
import it.nextworks.nfvmano.elm.nbi.messages.ExecuteExperimentRequest;
import it.nextworks.nfvmano.elm.nbi.messages.ExperimentSchedulingRequest;
import it.nextworks.nfvmano.elm.nbi.messages.ModifyExperimentTimeslotRequest;
import it.nextworks.nfvmano.elm.nbi.messages.UpdateExperimentStatusRequest;
import it.nextworks.nfvmano.elm.sbi.eem.EemService;
import it.nextworks.nfvmano.elm.sbi.eem.interfaces.EemConsumerInterface;
import it.nextworks.nfvmano.elm.sbi.eem.messages.ExperimentExecutionStatusChangeNotification;
import it.nextworks.nfvmano.elm.sbi.expcatalogue.SbiExperimentCatalogueService;
import it.nextworks.nfvmano.elm.sbi.monitoring.DataCollectionManagerDriver;
import it.nextworks.nfvmano.elm.sbi.ticketing.TicketingSystemService;
import it.nextworks.nfvmano.libs.ifa.common.elements.Filter;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MalformattedElementException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MethodNotImplementedException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.NotExistingEntityException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.NotPermittedOperationException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.WrongStatusException;
import it.nextworks.nfvmano.libs.ifa.common.messages.GeneralizedQueryRequest;
import it.nextworks.nfvmano.nfvodriver.NfvoLcmNotificationConsumerInterface;
import it.nextworks.nfvmano.nfvodriver.NfvoLcmService;
import it.nextworks.nfvmano.nfvodriver.NsStatusChange;

import javax.annotation.PostConstruct;


@Service

public class ExperimentLifecycleManagerEngine 
implements ExperimentLifecycleManagerProviderInterface, NfvoLcmNotificationConsumerInterface, EemConsumerInterface {

	private static final Logger log = LoggerFactory.getLogger(ExperimentLifecycleManagerEngine.class);

	@Value("${spring.rabbitmq.host}")
	private String rabbitHost;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	@Qualifier(ConfigurationParameters.elmQueueExchange)
	TopicExchange messageExchange;
	
	//Key: experiment ID; Value: EIM
	private Map<String, ExperimentInstanceManager> experimentInstances = new HashMap<String, ExperimentInstanceManager>();
	
	@Autowired
	private ExperimentRecordsManager experimentRecordManager;
	
	@Autowired
	private SbiExperimentCatalogueService sbiExperimentCatalogueService;
	
	@Autowired
	private NfvoLcmService nfvoLcmService;
	
	@Autowired
	private TicketingSystemService ticketingSystemService;
	
	@Autowired
	private DataCollectionManagerDriver dcmDriver;
	
	@Autowired
	private EemService eemService;


    @Value("#{${ticketing.addresses}}")
    private Map<String, String> ticketingAddresses;

	/**
	 * Since ExpDs cannot be retrieved on PostConstruct due to the need of the authentication principal
	 * the following list contains the list of instance managers that need to be updated after being
	 * recreated
	 */

	private List<String> recreatedInstanceManagers = new ArrayList<>();
	
	public ExperimentLifecycleManagerEngine() {	
		//TODO: rebuild experimet map from db when restarting
	}
	
	@Override
	public String scheduleNewExperiment(ExperimentSchedulingRequest request, String tenantId, String tenantEmail)
			throws NotExistingEntityException, MalformattedElementException, FailedOperationException, MethodNotImplementedException {
		request.isValid();
		String expDescriptorId = request.getExperimentDescriptorId();
		log.debug("Received request to schedule a new experiment for experiment descriptor " + expDescriptorId);
		//check if the experiment descriptor exists
		Map<String,String> parameters = new HashMap<>();
		parameters.put("ExpD_ID", expDescriptorId);
		Filter filter = new Filter(parameters);
		QueryExpDescriptorResponse expD = sbiExperimentCatalogueService.queryExpDescriptor(new GeneralizedQueryRequest(filter, null));
		if (expD.getExpDescriptors().isEmpty()) {
			log.debug("Experiment Descriptor Not Found");
			throw new NotExistingEntityException("Experiment Descriptor Not Found");
		}
		String experimentId = experimentRecordManager.createExperiment(expDescriptorId, request.getExperimentName(), tenantId, request.getProposedTimeSlot(), request.getTargetSites());
		try {

			sbiExperimentCatalogueService.useExpDescriptor(request.getExperimentDescriptorId(), experimentId);
			initNewExperimentInstanceManager(experimentId, expD.getExpDescriptors().get(0), tenantId, request.getTargetSites());
			String topic = "lifecycle.schedule." + experimentId;
			ScheduleExperimentInternalMessage internalMessage = new ScheduleExperimentInternalMessage(experimentId, request, tenantEmail);
			try {
				sendMessageToQueue(internalMessage, topic);
			} catch (JsonProcessingException e) {
				log.error("Error while translating internal scheduling message in Json format.");
				this.experimentInstances.remove(experimentId);
				throw new FailedOperationException("Internal error with queues.");
			}
			return experimentId;
		}catch(Exception e){
			log.error("Error assigning experiment:"+experimentId+" to ExpD:"+expDescriptorId, e);
			throw new FailedOperationException("Error assigning experiment:"+experimentId+" to ExpD:"+expDescriptorId, e);
		}
	}

	@Override
	public List<Experiment> getExperiments(GeneralizedQueryRequest request)
			throws MalformattedElementException, FailedOperationException, MethodNotImplementedException, NotExistingEntityException {
		request.isValid();
		log.debug("Received query for experiments.");
		
		//At the moment the only filter accepted are:
		//1. TENANT ID
		//2. TENANT ID and Exp_ID
		//3. TENANT ID and ExpD_ID
		//No attribute selector is supported at the moment
				
		Filter filter = request.getFilter();
		List<String> attributeSelector = request.getAttributeSelector();
		
		if ((attributeSelector == null) || (attributeSelector.isEmpty())) {
			Map<String,String> fp = filter.getParameters();
			if (fp.size()==1 && fp.containsKey("TENANT_ID")) {
				String tenantId = fp.get("TENANT_ID");
				log.debug("Received a query about experiments for tenant ID " + tenantId);
				return experimentRecordManager.retrieveAllExperiments(tenantId);
			} else if (fp.size()==2 && fp.containsKey("TENANT_ID") && fp.containsKey("ExpD_ID")) {
				String tenantId = fp.get("TENANT_ID");
				String expDescriptorId = fp.get("ExpD_ID");
				log.debug("Received a query about experiments associated to experiment descriptor " + expDescriptorId + " for tenant ID " + tenantId);
				return experimentRecordManager.retrieveExperimentsFromExpDescriptorId(expDescriptorId, tenantId);
			} else if (fp.size()==2 && fp.containsKey("TENANT_ID") && fp.containsKey("Exp_ID")) {
				String tenantId = fp.get("TENANT_ID");
				String experimentId = fp.get("Exp_ID");
				log.debug("Received a query about experiment " + experimentId + " for tenant ID " + tenantId);
				Experiment experiment = experimentRecordManager.retrieveExperimentFromIdAndTenant(experimentId, tenantId);
				List<Experiment> exps = new ArrayList<Experiment>();
				exps.add(experiment);
				return exps;
			} else {
				log.error("Received experiment query with not supported filter.");
				throw new MalformattedElementException("Received experiment query with not supported filter.");
			}
		} else {
			log.error("Received experiment query with attribute selector. Not supported at the moment.");
			throw new MethodNotImplementedException("Received experiment query with attribute selector. Not supported at the moment.");
		}
	}

	@Override
	public void updateExperimentStatus(UpdateExperimentStatusRequest request, String tenantId, String tenantEmail)
			throws NotExistingEntityException, MalformattedElementException, FailedOperationException, WrongStatusException, NotPermittedOperationException, MethodNotImplementedException {
		request.isValid();
		String experimentId = request.getExperimentId();
		ExperimentStatus targetStatus = request.getStatus();
		log.debug("Received request to update the state of experiment " + experimentId + " to state " + targetStatus.toString());
		ExperimentInstanceManager eim = getExperimentInstanceManager(experimentId);
		ExperimentStatus currentStatus = eim.getExperimentStatus();
		if (! ((currentStatus == ExperimentStatus.SCHEDULING) || (currentStatus == ExperimentStatus.ACCEPTED))) {
			log.error("Experiment " + experimentId + " is not in scheduling or in accepted state.");
			throw new WrongStatusException("Experiment " + experimentId + " is not in scheduling or in accepted state.");
		} else if (currentStatus == ExperimentStatus.SCHEDULING) {
			if ( !((targetStatus == ExperimentStatus.ACCEPTED) || (targetStatus == ExperimentStatus.REFUSED))) {
				log.error("Experiment " + experimentId + " is in SCHEDULING state. Target state " + targetStatus.toString() + " is not acceptable.");
				throw new NotPermittedOperationException("Experiment " + experimentId + " is in SCHEDULING state. Target status " + targetStatus.toString() + " is not acceptable.");
			} 
		} else {
			//currentStatus == ExperimentStatus.ACCEPTED 
			if ( !(targetStatus == ExperimentStatus.READY)) {
				log.error("Experiment " + experimentId + " is in ACCEPTED state. Target state " + targetStatus.toString() + " is not acceptable.");
				throw new NotPermittedOperationException("Experiment " + experimentId + " is in ACCEPTED state. Target state " + targetStatus.toString() + " is not acceptable.");
			}
		}
		//TODO: check the permission based on the tenant role. This action should be permitted only to the site manager of the facility where the experiment will run
		String topic = "lifecycle.changestate." + experimentId;
		UpdateExperimentStateInternalMessage internalMessage = new UpdateExperimentStateInternalMessage(request, tenantEmail);
		try {
			sendMessageToQueue(internalMessage, topic);
		} catch (JsonProcessingException e) {
			log.error("Error while translating internal change state message in Json format.");
			throw new FailedOperationException("Internal error with queues.");
		}
		
	}

	@Override
	public void updateExperimentTimeslot(ModifyExperimentTimeslotRequest request, String tenantId)
			throws NotExistingEntityException, MalformattedElementException, FailedOperationException, WrongStatusException, MethodNotImplementedException {
		//TODO:
		throw new MethodNotImplementedException();
	}

	@Override
	public void deployExperiment(String experimentId, String tenantId) 
			throws NotExistingEntityException, MalformattedElementException, FailedOperationException, WrongStatusException, NotPermittedOperationException, MethodNotImplementedException {
		if (experimentId == null) throw new MalformattedElementException("Received deploy experiment request without experiment ID");
		log.debug("Received request to deploy experiment " + experimentId);
		ExperimentInstanceManager eim = getExperimentInstanceManager(experimentId);
		checkExperimenterPermission(eim, tenantId);
		ExperimentStatus currentStatus = eim.getExperimentStatus();
		if (currentStatus != ExperimentStatus.READY) {
			log.error("Experiment " + experimentId + " cannot be deployed since the environment is not ready.");
			throw new WrongStatusException("Experiment " + experimentId + " cannot be deployed since the environment is not ready.");
		}
		//check the timing
		Experiment experiment = experimentRecordManager.retrieveExperimentFromIdAndTenant(experimentId, tenantId);
		OffsetDateTime currentTime = OffsetDateTime.now();
		if (!(experiment.canBeExecutedAtTime(currentTime))) {
			log.error("Experiment " + experimentId + " cannot be deployed since the current time is not included in the available timeslot");
			throw new NotPermittedOperationException("Experiment " + experimentId + " cannot be deployed since the current time is not included in the available timeslot");
		}
		
		String topic = "lifecycle.deploy." + experimentId;
		DeployExperimentInternalMessage internalMessage = new DeployExperimentInternalMessage();
		try {
			sendMessageToQueue(internalMessage, topic);
		} catch (JsonProcessingException e) {
			log.error("Error while translating internal deploy command message in Json format.");
			throw new FailedOperationException("Internal error with queues.");
		}
	}
	
	@Override
	public void executeExperiment(ExecuteExperimentRequest request, String tenantId) 
			throws NotExistingEntityException, MalformattedElementException, FailedOperationException, WrongStatusException, NotPermittedOperationException, MethodNotImplementedException {
		request.isValid();
		String experimentId = request.getExperimentId();
		log.debug("Received request to execute experiment " + experimentId);
		ExperimentInstanceManager eim = getExperimentInstanceManager(experimentId);
		checkExperimenterPermission(eim, tenantId);
		ExperimentStatus currentStatus = eim.getExperimentStatus();
		if (currentStatus != ExperimentStatus.INSTANTIATED) {
			log.error("The system cannot launch a new execution for experiment " + experimentId + " since it is not in INSTANTIATED state.");
			throw new WrongStatusException("The system cannot launch a new execution for experiment " + experimentId + " since it is not in INSTANTIATED state.");
		}
		
		String topic = "lifecycle.execute." + experimentId;
		ExecuteExperimentInternalMessage internalMessage = new ExecuteExperimentInternalMessage(request);
		try {
			sendMessageToQueue(internalMessage, topic);
		} catch (JsonProcessingException e) {
			log.error("Error while translating internal deploy command message in Json format.");
			throw new FailedOperationException("Internal error with queues.");
		}
	}

	@Override
	public void terminateExperiment(String experimentId, String tenantId)
			throws NotExistingEntityException, MalformattedElementException, FailedOperationException, WrongStatusException, NotPermittedOperationException, MethodNotImplementedException {
		if (experimentId == null) throw new MalformattedElementException("Received request to terminate an experiment with null ID.");
		log.debug("Received request to terminate experiment " + experimentId);
		ExperimentInstanceManager eim = getExperimentInstanceManager(experimentId);
		checkExperimenterPermission(eim, tenantId);
		ExperimentStatus currentStatus = eim.getExperimentStatus();
		if (currentStatus != ExperimentStatus.INSTANTIATED) {
			log.error("The system cannot terminate experiment " + experimentId + " since it is not in INSTANTIATED state.");
			throw new WrongStatusException("The system cannot terminate experiment " + experimentId + " since it is not in INSTANTIATED state.");
		}
		String topic = "lifecycle.terminate." + experimentId;
		TerminateExperimentInternalMessage internalMessage = new TerminateExperimentInternalMessage();
		try {
			sendMessageToQueue(internalMessage, topic);
		} catch (JsonProcessingException e) {
			log.error("Error while translating internal deploy command message in Json format.");
			throw new FailedOperationException("Internal error with queues.");
		}
	}
	
	@Override
	public void purgeExperiment(String experimentId, String tenantId)
			throws NotExistingEntityException, MalformattedElementException, FailedOperationException, WrongStatusException, NotPermittedOperationException, MethodNotImplementedException {
		if (experimentId == null) throw new MalformattedElementException("Received request to terminate an experiment with null ID.");
		log.debug("Received request to remove experiment " + experimentId);
		ExperimentInstanceManager eim = getExperimentInstanceManager(experimentId);
		checkExperimenterPermission(eim, tenantId);
		ExperimentStatus currentStatus = eim.getExperimentStatus();
		if (! ((currentStatus == ExperimentStatus.TERMINATED) || (currentStatus == ExperimentStatus.FAILED) || (currentStatus == ExperimentStatus.REFUSED))) {
			log.error("The system cannot remove experiment " + experimentId + " since it is not in TERMINATED or FAILED state.");
			throw new WrongStatusException("The system cannot terminate experiment " + experimentId + " since it is not in TERMINATED or FAILED state.");
		}
		Experiment exp = experimentRecordManager.retrieveExperimentFromId(experimentId);
		String expdId = exp.getExperimentDescriptorId();
		log.debug("Releasing ExpD: "+expdId+" from experiment:"+experimentId);
		try{
			sbiExperimentCatalogueService.releaseExpDescriptor(expdId, experimentId);
		}catch (Exception e){
			log.error("Error assigning experiment:"+experimentId+" to ExpD:"+expdId, e);
			throw new FailedOperationException("Error assigning experiment:"+experimentId+" to ExpD:"+expdId, e);
		}
		experimentInstances.remove(experimentId);
		experimentRecordManager.removeExperiment(experimentId);
	}

	@Override
	public void abortExperiment(String experimentId, String tenantId)
			throws NotExistingEntityException, MalformattedElementException, FailedOperationException, WrongStatusException, MethodNotImplementedException {
		//TODO:
		throw new MethodNotImplementedException("Experiment abortiion not yet supported.");
	}
	
	@Override
	public void notifyNfvNsStatusChange(String nfvNsId, NsStatusChange changeType, boolean b) {
		log.debug("Processing notification about status change for NFV NS " + nfvNsId);
		try {
			Experiment exp = experimentRecordManager.retrieveExperimentFromNsId(nfvNsId);
			String expId = exp.getExperimentId();
			log.debug("Notification related to experiment " + expId);
			NotifyNfvNsStatusChangeInternalMessage internalMessage = new NotifyNfvNsStatusChangeInternalMessage(nfvNsId, changeType, b);
			String topic = "lifecycle.notifyns." + expId;
			sendMessageToQueue(internalMessage, topic);
		} catch (NotExistingEntityException e) {
			log.error("Unable to process the notification: " + e.getMessage() + ". Skipping message.");
		} catch (Exception e) {
			log.error("General exception while processing notification: " + e.getMessage());
		}
	}
	
	@Override
	public void notifyExperimentExecutionStatusChange(ExperimentExecutionStatusChangeNotification msg) {
		log.debug("Processing notification from EEM about change in experiment execution status");
		try {
			msg.isValid();
			String executionId = msg.getExperimentExecutionId();
			ExperimentExecution exec = experimentRecordManager.retrieveExperimentExecution(executionId);
			String experimentId = exec.getExperiment().getExperimentId();
			log.debug("Received notification associated to execution " + executionId + " in experiment " + experimentId);
			ExperimentInstanceManager eim = getExperimentInstanceManager(experimentId);
			if (eim.getExperimentStatus() != ExperimentStatus.RUNNING_EXECUTION) {
				log.error("Received notification about an execution for an experiment in not running status. Skipping");
				return;
			}
			EemNotificationInternalMessage internalMessage = new EemNotificationInternalMessage(msg);
			String topic = "lifecycle.notifyeem." + experimentId;
			sendMessageToQueue(internalMessage, topic);
		} catch (MalformattedElementException e) {
			log.error("Received malformed notification from EEM. Skipping it.");
		} catch (NotExistingEntityException e) {
			log.error("Experiment execution or experiment not found in internal DB. Skipping notification.");
		} catch (Exception e) {
			log.error("General exception while processing notification: " + e.getMessage());
		}
	}
	
	private ExperimentInstanceManager getExperimentInstanceManager(String experimentId) throws NotExistingEntityException {
		ExperimentInstanceManager eim = experimentInstances.get(experimentId);
		if (eim == null) {
			log.error("Experiment " + experimentId + " does not exist.");
			throw new NotExistingEntityException("Experiment " + experimentId + " does not exist.");
		}

		if(recreatedInstanceManagers.contains(experimentId)){
			boolean recovered =recoverExperimentInstanceManager(experimentId);
			if(recovered) recreatedInstanceManagers.remove(experimentId);
		}
		return eim;
	}
	
	private void checkExperimenterPermission(ExperimentInstanceManager eim, String tenantId) throws NotPermittedOperationException {
		if (!(eim.getTenantId().equals(tenantId))) {
			log.error("Experiment " + eim.getExperimentId() + " cannot be deployed by tenant " + tenantId);
			throw new NotPermittedOperationException("Experiment " + eim.getExperimentId() + " cannot be deployed by tenant " + tenantId);
		}
	}
	
	private void initNewExperimentInstanceManager(String experimentId, ExpDescriptor experimentDescriptor, String tenantId, List<EveSite> targetSites) {
		log.debug("Initializing new experiment instance manager with id " + experimentId);
		ExperimentInstanceManager eim = new ExperimentInstanceManager(
				experimentId,
				experimentDescriptor, 
				tenantId,
				targetSites,
				this,
				experimentRecordManager,
				ticketingSystemService,
				sbiExperimentCatalogueService,
				nfvoLcmService,
				dcmDriver,
				eemService,
                true,
                ticketingAddresses);
		createQueue(experimentId, eim);
		experimentInstances.put(experimentId, eim);
		log.debug("Experiment instance manager for ID " + experimentId + " initialized.");
	}
	
	private void createQueue(String experimentId, ExperimentInstanceManager eim) {
		String queueName = ConfigurationParameters.elmQueueNamePrefix + experimentId;
		log.debug("Creating new Queue " + queueName + " in rabbit host " + rabbitHost);
		CachingConnectionFactory cf = new CachingConnectionFactory();
		cf.setAddresses(rabbitHost);
		cf.setConnectionTimeout(5);
		RabbitAdmin rabbitAdmin = new RabbitAdmin(cf);
		Queue queue = new Queue(queueName, false, false, true);
		rabbitAdmin.declareQueue(queue);
		rabbitAdmin.declareExchange(messageExchange);
		rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(messageExchange).with("lifecycle.*." + experimentId));
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(cf);
		MessageListenerAdapter adapter = new MessageListenerAdapter(eim, "receiveMessage");
		container.setMessageListener(adapter);
	    container.setQueueNames(queueName);
	    container.start();
	    log.debug("Queue created");
	}
	
	private void sendMessageToQueue(InternalMessage msg, String topic) throws JsonProcessingException {
		ObjectMapper mapper = buildObjectMapper();
		String json = mapper.writeValueAsString(msg);
		rabbitTemplate.convertAndSend(messageExchange.getName(), topic, json);
	}
	
	private ObjectMapper buildObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.setSerializationInclusion(Include.NON_EMPTY);
		mapper.registerModule(new JavaTimeModule());
		return mapper;
	}

	private void initActiveExperimentInstanceManager(String experimentId,
													 List<String> lcTicketId,
													 String nsInstanceId,
													 String currentExecutionId,
													 String eemSubscriptionId,
													 String msnoSubscriptionId,
													 ExperimentStatus status, ExpDescriptor experimentDescriptor, String tenantId, List<EveSite> targetSites) {
		log.debug("Initializing active experiment instance manager with id " + experimentId);
		ExperimentInstanceManager eim = new ExperimentInstanceManager(
				experimentId,
				experimentDescriptor,
				tenantId,
				targetSites,
				status,
				lcTicketId,
				nsInstanceId,
				currentExecutionId,
				eemSubscriptionId,
				msnoSubscriptionId,
				this,
				experimentRecordManager,
				ticketingSystemService,
				sbiExperimentCatalogueService,
				nfvoLcmService,
				dcmDriver,
				eemService,
                false,
                ticketingAddresses
		);
		createQueue(experimentId, eim);
		experimentInstances.put(experimentId, eim);
		log.debug("Experiment instance manager for ID " + experimentId + " initialized.");
	}


	private boolean recoverExperimentInstanceManager(String experimentId) throws NotExistingEntityException {
		log.debug("Recovering Experiment Instance Manager ExpD for: "+experimentId);
		ExperimentInstanceManager eim = this.experimentInstances.get(experimentId);
		QueryExpDescriptorResponse expD = null;
		Experiment experiment = experimentRecordManager.retrieveExperimentFromId(experimentId);
		Map<String,String> parameters = new HashMap<>();
		parameters.put("ExpD_ID", experiment.getExperimentDescriptorId());
		Filter filter = new Filter(parameters);
		try {
				expD = sbiExperimentCatalogueService.queryExpDescriptor(new GeneralizedQueryRequest(filter, null));
				eim.loadInformationFromPortalCatalogue(expD.getExpDescriptors().get(0));
				return true;
		} catch (Exception e) {
				log.error("Error retrieving ExperimentDescriptor from active Experiment!",e);
				return false;
		}

	}


	@PostConstruct
	private void recreateExperimentInstanceManagersFromDB(){
		log.debug("Recreating Experiment Instance Managers From DB");
		List<Experiment> activeExperiments = experimentRecordManager.retrieveAllActiveExperiments();
		if(activeExperiments.isEmpty()) log.debug("No active experiments retrieved");
		for (Experiment current: activeExperiments){
			log.debug("Recreating Experiment Instance Manager for: "+current);
			String experimentId = current.getExperimentId();
			//String expDescriptorId = current.getExperimentDescriptorId();

			ExpDescriptor experimentDescriptor = null;
			ExperimentStatus status = current.getStatus();
			List<String> lcTicketId = current.getOpenTicketIds();
			String nsInstanceId = current.getNfvNsInstanceId();
			String currentExecutionId  = current.getCurrentExecutionId();
			String eemSubscriptionId = current.getCurrentEemSubscriptionId();
			String msnoSubscriptionId = current.getCurrentMsnoSubscriptionId();
			initActiveExperimentInstanceManager(experimentId,
					lcTicketId,
					nsInstanceId,
					currentExecutionId,
					eemSubscriptionId,
					msnoSubscriptionId,
					status,
					null, //To be recovered later
					current.getTenantId(),
					current.getTargetSites());
			recreatedInstanceManagers.add(experimentId);
			log.debug("Succesfully recreated Experiment Instance Managers for experiment:"+experimentId);

		}
		log.debug("Finished recreating Experiment Instance Managers");

	}
}
