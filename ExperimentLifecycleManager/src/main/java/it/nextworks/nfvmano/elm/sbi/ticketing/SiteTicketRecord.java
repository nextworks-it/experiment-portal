package it.nextworks.nfvmano.elm.sbi.ticketing;

public class SiteTicketRecord {

    private String ticketSiteId;
    private String reporter;


    public SiteTicketRecord(String ticketSiteId, String reporter){
        this.ticketSiteId=ticketSiteId;
        this.reporter=reporter;

    }


    public String getTicketSiteId() {
        return ticketSiteId;
    }

    public String getReporter() {
        return reporter;
    }
}
