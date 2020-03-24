package it.nextworks.nfvmano.elm.sbi.ticketing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.CommentsApi;
import io.swagger.client.api.TicketsApi;
import io.swagger.client.model.NewTrustedComment;
import io.swagger.client.model.NewTrustedTicket;
import io.swagger.client.model.TicketResponse;
import it.nextworks.nfvmano.catalogue.blueprint.elements.EveSite;
import it.nextworks.nfvmano.catalogue.blueprint.elements.ExpDescriptor;
import it.nextworks.nfvmano.elm.im.Experiment;
import it.nextworks.nfvmano.elm.im.ExperimentExecutionTimeslot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;



public class TicketingSystemRestClient implements TicketingRestInterface{

	private static final Logger log = LoggerFactory.getLogger(TicketingSystemRestClient.class);


	private TicketsApi ticketingClient;

	private CommentsApi commentsApi;


	private String ticketingUrl;


	private Map<String, String> ticketingAddresses;


	private Map<String, ArrayList<SiteTicketRecord>> mapTicketToSiteTicket = new HashMap<>();

	public TicketingSystemRestClient(String ticketingUrl, Map<String, String> ticketingAddresses){
		this.ticketingUrl=ticketingUrl;
		this.ticketingAddresses = ticketingAddresses;
		ticketingClient = new TicketsApi();
		commentsApi = new CommentsApi();
		ApiClient apiClient = new ApiClient();
		//apiClient.setDebugging(true);
		apiClient.setBasePath(ticketingUrl);
		ticketingClient.setApiClient(apiClient);
		commentsApi.setApiClient(apiClient);

	}
	
	public String createSchedulingTicket(Experiment experiment,
										 ExpDescriptor experimentDescriptor,
										 ExperimentExecutionTimeslot timeslot) throws TicketOperationException {
		log.debug("Invoking ticketing system to schedule a new experiment.");
		UUID ticketUuid = UUID.randomUUID();
		String ticketId = ticketUuid.toString();
		String componentName = "EXPERIMENT_SCHEDULE";
		TicketDescription td = new TicketDescription(experiment.getExperimentId(), timeslot);
		ObjectMapper mapper = new ObjectMapper();
		String description  = "";
		ArrayList<SiteTicketRecord> siteTicketIds = new ArrayList<>();
		try {
			description = mapper.writeValueAsString(td);
		} catch (JsonProcessingException e) {
			throw new TicketOperationException("Failed to render ticket description:"+e.getMessage());
		}
		for(EveSite eveSite: experiment.getTargetSites()){
			String productName = eveSite.name();
			if(ticketingAddresses.containsKey(productName) ){
				String assignedTo = ticketingAddresses.get(productName);
				NewTrustedTicket ticket = new NewTrustedTicket();
				ticket.reporter(assignedTo);
				ticket.setAssignedTo(assignedTo);
				ticket.setComponent(componentName);
				ticket.description(description);
				ticket.product(productName);
				ticket.summary(experiment.getName()+" SLOT_REQUEST");

				try {

					ApiResponse response = ticketingClient.addTrustedTicketWithHttpInfo(ticket);

					String siteTicketId = ((TicketResponse)response.getData()).getDetails().getId();
					log.debug("Generated ticket with Id: " + ticketId+" site id:"+siteTicketId+" site:"+productName);
					siteTicketIds.add(new SiteTicketRecord(siteTicketId,assignedTo));
				} catch (ApiException e) {
					log.error("Failed API:", e);
					throw new TicketOperationException("Failed to create ticket via API:"+e.getMessage());
				}
			}else throw new TicketOperationException("Failed to retrieve email for site:"+productName);

		}

		log.debug("Generated ticket with ID: " + ticketId);
		mapTicketToSiteTicket.put(ticketId, siteTicketIds);
		return ticketId;
	}
	
	public void updateSchedulingTicket(String ticketId, LcTicketUpdateType updateType) throws TicketOperationException {
		log.debug("Invoking ticketing system to update ticket " + ticketId + " for reason " + updateType.toString());
		if(mapTicketToSiteTicket.containsKey(ticketId)){
			for(SiteTicketRecord siteRecord: mapTicketToSiteTicket.get(ticketId)){
				log.debug("Updating ticket id:"+ticketId+" site ticket id:"+siteRecord.getTicketSiteId()+"with status: "+updateType);
				NewTrustedComment comment = new NewTrustedComment();
				comment.comment(updateType.toString());
				comment.reporter(siteRecord.getReporter());
				try {
					commentsApi.ticketsTicketIdCommentsTrustedPostWithHttpInfo(comment, siteRecord.getTicketSiteId() );
					log.debug("Succesfully updated comment for ticket:"+ticketId+" site_id:"+siteRecord.getTicketSiteId());
				} catch (ApiException e) {
					log.error("Error using Comments API!", e);
					throw new TicketOperationException("Error using Comments API");
				}
			}
			log.debug("Succesfuly updated all comment for ticket:"+ticketId);
		}else throw new TicketOperationException("Unknown ticket id:"+ticketId);
	}



}
