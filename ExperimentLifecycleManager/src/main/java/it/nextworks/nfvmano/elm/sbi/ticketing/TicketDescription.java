package it.nextworks.nfvmano.elm.sbi.ticketing;


import it.nextworks.nfvmano.elm.im.ExperimentExecutionTimeslot;
public class TicketDescription {


    private String experimentId;

    private ExperimentExecutionTimeslot experimentExecutionTimeSlot;


    public TicketDescription(){}

    public TicketDescription(String experimentId, ExperimentExecutionTimeslot experimentExecutionTimeslot){

        this.experimentExecutionTimeSlot=experimentExecutionTimeslot;
        this.experimentId = experimentId;
    }


    public String getExperimentId() {
        return experimentId;
    }

    public ExperimentExecutionTimeslot getExperimentExecutionTimeSlot() {
        return experimentExecutionTimeSlot;
    }
}

