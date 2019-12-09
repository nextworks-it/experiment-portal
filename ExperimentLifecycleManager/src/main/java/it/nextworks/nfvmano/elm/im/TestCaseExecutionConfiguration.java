package it.nextworks.nfvmano.elm.im;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
public class TestCaseExecutionConfiguration {

	@Id
    @GeneratedValue
    @JsonIgnore
    private Long id;
	
	@JsonIgnore
	@ManyToOne
	private ExperimentExecution execution;
	
	private String tcDescriptorId;

	//the value of the map is a map with the user parameters to be overwritten for the given run
	//the format of the map in the value field is the same as in the test case descriptor: 
	//Key: parameter name, as in the key of the corresponding map in the blueprint; value: desired value
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@ElementCollection(fetch=FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private Map<String, String> execConfiguration = new HashMap<String, String>();
	
	public TestCaseExecutionConfiguration() { }

	
	
	/**
	 * @param tcDescriptorId
	 * @param execConfiguration
	 */
	public TestCaseExecutionConfiguration(ExperimentExecution execution, String tcDescriptorId, Map<String, String> execConfiguration) {
		this.execution = execution;
		this.tcDescriptorId = tcDescriptorId;
		if (execConfiguration != null) this.execConfiguration = execConfiguration;
	}



	/**
	 * @return the tcDescriptorId
	 */
	public String getTcDescriptorId() {
		return tcDescriptorId;
	}

	/**
	 * @return the execConfiguration
	 */
	public Map<String, String> getExecConfiguration() {
		return execConfiguration;
	}
	
	

}
