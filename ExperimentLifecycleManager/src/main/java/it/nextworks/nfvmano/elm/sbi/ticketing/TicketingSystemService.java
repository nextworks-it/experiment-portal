package it.nextworks.nfvmano.elm.sbi.ticketing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.CommentsApi;
import io.swagger.client.api.TicketsApi;
import io.swagger.client.model.NewTicket;
import io.swagger.client.model.NewTrustedComment;
import io.swagger.client.model.NewTrustedTicket;
import it.nextworks.nfvmano.catalogue.blueprint.elements.EveSite;
import it.nextworks.nfvmano.elm.im.Experiment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import it.nextworks.nfvmano.catalogue.blueprint.elements.ExpDescriptor;
import it.nextworks.nfvmano.elm.im.ExperimentExecutionTimeslot;

import javax.annotation.PostConstruct;


@Service
public class TicketingSystemService {

	private static final Logger log = LoggerFactory.getLogger(TicketingSystemService.class);


	private TicketingRestInterface restClient;

	@Value("${ticketing.type}")
	private TicketingType ticketingType;

	@Value("${ticketing.url}")
	private String ticketingUrl;

	@Value("#{${ticketing.addresses}}")
	private Map<String, String> ticketingAddresses;


	private Map<String, ArrayList<String>> mapTicketToSiteTicket = new HashMap<>();
	public TicketingSystemService() {
		// TODO Auto-generated constructor stub
	}
	
	public String createSchedulingTicket(Experiment experiment,
										 ExpDescriptor experimentDescriptor,
										 ExperimentExecutionTimeslot timeslot) throws TicketOperationException {
		return restClient.createSchedulingTicket(experiment, experimentDescriptor, timeslot);
	}
	
	public void updateSchedulingTicket(String ticketId, LcTicketUpdateType updateType) throws TicketOperationException {

		restClient.updateSchedulingTicket(ticketId, updateType);
	}

	@PostConstruct
	public void setupTicketingService(){
		if(ticketingType==TicketingType.BUGZILLA){
			log.debug("Configuring BUGZILLA TICKETING CLIENT");
			restClient= new TicketingSystemRestClient(ticketingUrl, ticketingAddresses);
		}else if(ticketingType==TicketingType.DUMMY){
			log.debug("Configuring DUMMY TICKETING CLIENT");
			restClient= new TicketingDummyDriver();
		}


	}

}
