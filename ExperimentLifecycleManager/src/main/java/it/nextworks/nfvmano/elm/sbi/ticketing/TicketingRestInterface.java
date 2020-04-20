package it.nextworks.nfvmano.elm.sbi.ticketing;

import it.nextworks.nfvmano.catalogue.blueprint.elements.ExpDescriptor;
import it.nextworks.nfvmano.elm.im.Experiment;
import it.nextworks.nfvmano.elm.im.ExperimentExecutionTimeslot;

public interface TicketingRestInterface {

    String createSchedulingTicket(Experiment experiment,
                                         ExpDescriptor experimentDescriptor,
                                         ExperimentExecutionTimeslot timeslot, String eveSiteName,  String siteAdminAddress, String reporter) throws TicketOperationException;

    void updateSchedulingTicket(String ticketId, LcTicketUpdateType updateType, String reporter) throws TicketOperationException;
}
