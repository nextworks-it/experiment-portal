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







	public TicketingSystemRestClient(String ticketingUrl){
		this.ticketingUrl=ticketingUrl;

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
										 ExperimentExecutionTimeslot timeslot, String eveSiteName,  String siteAdminAddress, String reporter) throws TicketOperationException {
		log.debug("Invoking ticketing system to schedule a new experiment.");
		UUID ticketUuid = UUID.randomUUID();

		String componentName = "EXPERIMENT_SCHEDULE";
		TicketDescription td = new TicketDescription(experiment.getExperimentId(), timeslot);
		ObjectMapper mapper = new ObjectMapper();
		String description  = "";

		try {
			description = mapper.writeValueAsString(td);
		} catch (JsonProcessingException e) {
			throw new TicketOperationException("Failed to render ticket description:"+e.getMessage());
		}

		String productName = eveSiteName;
		String assignedTo = siteAdminAddress;
		NewTrustedTicket ticket = new NewTrustedTicket();
		ticket.reporter(reporter);
		ticket.setAssignedTo(assignedTo);
		ticket.setComponent(componentName);
		ticket.description(description);
		ticket.product(productName);
		ticket.summary(experiment.getName()+" SLOT_REQUEST");

		try {

			ApiResponse response = ticketingClient.addTrustedTicketWithHttpInfo(ticket);

			String ticketId = ((TicketResponse)response.getData()).getDetails().getId();
			log.debug("Generated ticket with Id: " + ticketId+" for site:"+productName);
			return ticketId;
		} catch (ApiException e) {
			log.error("Failed API:", e);
			throw new TicketOperationException("Failed to create ticket via API:"+e.getMessage());
		}

	}
	
	public void updateSchedulingTicket(String ticketId, LcTicketUpdateType updateType, String reporter) throws TicketOperationException {
		log.debug("Invoking ticketing system to update ticket " + ticketId + " for reason " + updateType.toString());


		log.debug("Updating ticket id:"+ticketId+"with status: "+updateType);
		NewTrustedComment comment = new NewTrustedComment();
		comment.comment(updateType.toString());
		comment.reporter(reporter);
		try {
			commentsApi.ticketsTicketIdCommentsTrustedPostWithHttpInfo(comment, ticketId );

		} catch (ApiException e) {
			log.error("Error using Comments API!", e);
			throw new TicketOperationException("Error using Comments API");
		}

		log.debug("Successfully updated all comment for ticket:"+ticketId);

	}



}
