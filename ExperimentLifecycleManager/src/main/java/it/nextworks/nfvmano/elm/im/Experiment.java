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
package it.nextworks.nfvmano.elm.im;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import it.nextworks.nfvmano.catalogue.blueprint.elements.EveSite;
import it.nextworks.nfvmano.elm.sbi.monitoring.MonitoringDataItem;

@Entity
public class Experiment {

	@Id
    @GeneratedValue
    @JsonIgnore
    private UUID id;

    private String experimentId;
    
    private String name;

    @JsonIgnore
    private String tenantId;
    
    private ExperimentStatus status;
    
    private String experimentDescriptorId;

    //Replaced  by the openTicketIds list
    //private String lcTicketId;
    
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
	@ElementCollection(fetch=FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private List<String> openTicketIds = new ArrayList<String>();
    
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
	@ElementCollection(fetch=FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private List<EveSite> targetSites = new ArrayList<EveSite>();
    
    @Embedded
    private ExperimentExecutionTimeslot timeslot;
    
    private String nfvNsInstanceId;
    
    private String currentExecutionId;

    @JsonIgnore
    private String currentEemSubscriptionId;


	@JsonIgnore
	private String currentMsnoSubscriptionId;

    private String errorMessage;
    
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
	@ElementCollection(fetch=FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private List<MonitoringDataItem> monitoringMetrics = new ArrayList<>();
	
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
	@ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(name="KPI")
	@Fetch(FetchMode.SELECT)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<MonitoringDataItem> monitoringKpis = new ArrayList<>();
    
    @OneToMany(mappedBy = "experiment", cascade=CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@LazyCollection(LazyCollectionOption.FALSE)
    private List<ExperimentExecution> executions = new ArrayList<>();


	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy = "experiment", cascade=CascadeType.ALL)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<ExperimentSapInfo> sapInfo = new ArrayList<>();


    private String useCase;

	public Experiment() {
		// JPA only
	}

    public Experiment(String experimentDescriptorId,
    		String name,
    		String tenantId,
    		ExperimentExecutionTimeslot timeslot,
    		List<EveSite> targetSites,
					  String useCase) {
    	this.tenantId = tenantId;
    	this.experimentDescriptorId = experimentDescriptorId;
    	this.name = name;
    	this.status = ExperimentStatus.SCHEDULING;
    	this.timeslot = timeslot;
    	if (targetSites != null) this.targetSites = targetSites;
    	this.useCase = useCase;
	}


	/**
	 * @return the status
	 */
	public ExperimentStatus getStatus() {
		return status;
	}



	/**
	 * @param status the status to set
	 */
	public void setStatus(ExperimentStatus status) {
		this.status = status;
	}



	/**
	 * @return the experimentDescriptorId
	 */
	public String getExperimentDescriptorId() {
		return experimentDescriptorId;
	}



	/**
	 * @param experimentDescriptorId the experimentDescriptorId to set
	 */
	public void setExperimentDescriptorId(String experimentDescriptorId) {
		this.experimentDescriptorId = experimentDescriptorId;
	}


	/**
	 * @param currentMsnoSubscriptionId the currentMsnoSubscriptionId to set
	 */
	public void setCurrentMsnoSubscriptionId(String currentMsnoSubscriptionId){
		this.currentMsnoSubscriptionId=currentMsnoSubscriptionId;
	}


	/**
	 * @return the currentMsnoSubscriptionId
	 */
	public String getCurrentMsnoSubscriptionId() {
		return currentMsnoSubscriptionId;
	}


	/**
	 * @param currentEemSubscriptionId the currentEemSubscriptionId to set
	 */
	public void setCurrentEemSubscriptionId(String currentEemSubscriptionId){
		this.currentEemSubscriptionId=currentEemSubscriptionId;
	}


	/**
	 * @return the currentEemSubscriptionId
	 */
	public String getCurrentEemSubscriptionId() {
		return currentEemSubscriptionId;
	}

	/**
	 * @return the currentExecutionId
	 */
	public String getCurrentExecutionId() {
		return currentExecutionId;
	}

	/**
	 * @param currentExecutionId the currentExecutionId to set
	 */
	public void setCurrentExecutionId(String currentExecutionId) {
		this.currentExecutionId = currentExecutionId;
	}

	public List<ExperimentSapInfo> getSapInfo() {
		return sapInfo;
	}

	public void setSapInfo(List<ExperimentSapInfo> sapInfo) {
		this.sapInfo = sapInfo;
	}

	/**
	 * @return the tenantId
	 */
	public String getTenantId() {
		return tenantId;
	}




	/*
	 * @return the lcTicketId

	public String getLcTicketId() {
		return lcTicketId;
	}



	/**
	 * @param lcTicketId the lcTicketId to set

	public void setLcTicketId(String lcTicketId) {
		this.lcTicketId = lcTicketId;
	}
	 */


	/**
	 * @return the openTicketIds
	 */
	public List<String> getOpenTicketIds() {
		return openTicketIds;
	}



	/**
	 * @param openTicketIds the openTicketIds to set
	 */
	public void setOpenTicketIds(List<String> openTicketIds) {
		this.openTicketIds = openTicketIds;
	}



	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the timeslot
	 */
	public ExperimentExecutionTimeslot getTimeslot() {
		return timeslot;
	}



	/**
	 * @param timeslot the timeslot to set
	 */
	public void setTimeslot(ExperimentExecutionTimeslot timeslot) {
		this.timeslot = timeslot;
	}



	/**
	 * @return the nfvNsInstanceId
	 */
	public String getNfvNsInstanceId() {
		return nfvNsInstanceId;
	}



	/**
	 * @param nfvNsInstanceId the nfvNsInstanceId to set
	 */
	public void setNfvNsInstanceId(String nfvNsInstanceId) {
		this.nfvNsInstanceId = nfvNsInstanceId;
	}


	/**
	 * @return the monitoringMetrics
	 */
	public List<MonitoringDataItem> getMonitoringMetrics() {
		return monitoringMetrics;
	}

	/**
	 * @param monitoringMetrics the monitoringMetrics to set
	 */
	public void setMonitoringMetrics(List<MonitoringDataItem> monitoringMetrics) {
		this.monitoringMetrics = monitoringMetrics;
	}

	/**
	 * @return the monitoringKpis
	 */
	public List<MonitoringDataItem> getMonitoringKpis() {
		return monitoringKpis;
	}

	/**
	 * @param monitoringKpis the monitoringKpis to set
	 */
	public void setMonitoringKpis(List<MonitoringDataItem> monitoringKpis) {
		this.monitoringKpis = monitoringKpis;
	}

	/**
	 * @return the targetSites
	 */
	public List<EveSite> getTargetSites() {
		return targetSites;
	}

	/**
	 * @return the id
	 */
	public UUID getId() {
		return id;
	}



	/**
	 * @return the experimentId
	 */
	public String getExperimentId() {
		return experimentId;
	}


	/**
	 * @return the useCase
	 */
	public String getUseCase() {
		return useCase;
	}


	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @param experimentId the experimentId to set
	 */
	public void setExperimentId(String experimentId) {
		this.experimentId = experimentId;
	}


	public void addTicket(String ticketId) {
		this.openTicketIds.add(ticketId);
	}
	
	public void removeTicket(String ticketId) {
		if (this.openTicketIds.contains(ticketId)) this.openTicketIds.remove(ticketId);
	}
	
	public boolean canBeExecutedAtTime(OffsetDateTime time) {
		if (timeslot.includeTimeInstance(time)) return true;
		else return false;
	}

	/**
	 * @return the experiment executions list
	 */
	public List<ExperimentExecution> getExecutions() {
		return executions;
	}
}
