package it.nextworks.nfvmano.elm.sbi.ticketing;

import it.nextworks.nfvmano.catalogue.blueprint.elements.ExpDescriptor;
import it.nextworks.nfvmano.elm.im.Experiment;
import it.nextworks.nfvmano.elm.im.ExperimentExecutionTimeslot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class TicketingDummyDriver implements TicketingRestInterface {
    private static final Logger log = LoggerFactory.getLogger(TicketingDummyDriver.class);

    @Override
    public String createSchedulingTicket(Experiment experiment, ExpDescriptor experimentDescriptor, ExperimentExecutionTimeslot timeslot) throws TicketOperationException {
        log.debug("Creating dummy ticket");
        return UUID.randomUUID().toString();
    }

    @Override
    public void updateSchedulingTicket(String ticketId, LcTicketUpdateType updateType) throws TicketOperationException {
        log.debug("Updagint dummy ticket: " +ticketId);
    }
}
