package it.nextworks.nfvmano.elm.sbi.ticketing;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import it.nextworks.nfvmano.catalogue.blueprint.elements.ExpDescriptor;
import it.nextworks.nfvmano.elm.im.ExperimentExecutionTimeslot;

@Service
public class TicketingSystemService {

	private static final Logger log = LoggerFactory.getLogger(TicketingSystemService.class);
	
	public TicketingSystemService() {
		// TODO Auto-generated constructor stub
	}
	
	public String createSchedulingTicket(String experimentId,
			ExpDescriptor experimentDescriptor,
			ExperimentExecutionTimeslot timeslot) {
		log.debug("Invoking ticketing system to schedule a new experiment.");
		//TODO: to be implemented. At the moment it returns a fake value.
		UUID ticketUuid = UUID.randomUUID();
		String ticketId = ticketUuid.toString();
		log.debug("Generated ticket with ID: " + ticketId);
		return ticketId;
	}
	
	public void updateSchedulingTicket(String ticketId, LcTicketUpdateType updateType) {
		log.debug("Invoking ticketing system to update ticket " + ticketId + " for reason " + updateType.toString());
		//TODO: to be implemented
	}

}
