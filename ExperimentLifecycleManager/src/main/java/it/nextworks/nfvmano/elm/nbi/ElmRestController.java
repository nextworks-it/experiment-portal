package it.nextworks.nfvmano.elm.nbi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.nextworks.nfvmano.elm.engine.ExperimentLifecycleManagerEngine;
import it.nextworks.nfvmano.elm.im.Experiment;
import it.nextworks.nfvmano.elm.nbi.messages.ExecuteExperimentRequest;
import it.nextworks.nfvmano.elm.nbi.messages.ExperimentSchedulingRequest;
import it.nextworks.nfvmano.elm.nbi.messages.ModifyExperimentTimeslotRequest;
import it.nextworks.nfvmano.elm.nbi.messages.UpdateExperimentStatusRequest;
import it.nextworks.nfvmano.libs.ifa.common.elements.Filter;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MalformattedElementException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MethodNotImplementedException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.NotExistingEntityException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.NotPermittedOperationException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.WrongStatusException;
import it.nextworks.nfvmano.libs.ifa.common.messages.GeneralizedQueryRequest;

@RestController
@CrossOrigin
@RequestMapping("/portal/elm")
public class ElmRestController {

	private static final Logger log = LoggerFactory.getLogger(ElmRestController.class);
	
	@Autowired
	private ExperimentLifecycleManagerEngine engine;
	
	@Value("${authentication.enable}")
	private boolean authenticationEnable;
	
	@Value("${elm.admin}")
	private String adminTenant;
	
	private  String getUserFromAuth(Authentication auth) {
		if(authenticationEnable){
			Object principal = auth.getPrincipal();
			if (!UserDetails.class.isAssignableFrom(principal.getClass())) {
				throw new IllegalArgumentException("Auth.getPrincipal() does not implement UserDetails");
			}
			return ((UserDetails) principal).getUsername();
		}else return adminTenant;

	}

	private  boolean validateAuthentication(Authentication auth){
		return !authenticationEnable || auth!=null;

	}
	
	public ElmRestController() { }

	@RequestMapping(value = "/experiment", method = RequestMethod.POST)
	public ResponseEntity<?> createExperiment(@RequestBody ExperimentSchedulingRequest request, Authentication auth) {
		log.debug("Received request to schedule an experiment.");
		
		//TODO: To be improved once the final authentication platform is in place.

		//This was added to allow disabling the authentication system for testing purposes.
		if (!validateAuthentication(auth)){
			log.warn("Unable to retrieve request authentication information");
			return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
		}
		String user = getUserFromAuth(auth);
		
		try {
			String experimentId = engine.scheduleNewExperiment(request, user);
			return new ResponseEntity<String>(experimentId, HttpStatus.CREATED);
		} catch (NotExistingEntityException e) {
			log.error("Something not found: " + e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (MalformattedElementException e) {
			log.error("Malformatted request: " + e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			log.error("Internal exception: " + e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/experiment", method = RequestMethod.GET)
	public ResponseEntity<?> getAllExperimenters(@RequestParam(required = false) String expId, @RequestParam(required = false) String expDId, Authentication auth) {
		log.debug("Received request to retrieve info about experiments.");
		if(!validateAuthentication(auth)){
			log.warn("Unable to retrieve request authentication information");
			return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
		}
		String user = getUserFromAuth(auth);
		
		try {
			Filter filter = buildFilter(expId, expDId, user);
			GeneralizedQueryRequest request = new GeneralizedQueryRequest(filter, null);
			List<Experiment> experiments = engine.getExperiments(request);
			return new ResponseEntity<List<Experiment>>(experiments, HttpStatus.OK);
		} catch (MalformattedElementException e) {
			log.error("Malformatted request: " + e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			log.error("Internal exception: " + e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/experiment/{expId}/status", method = RequestMethod.PUT)
	public ResponseEntity<?> updateExperimentStatus(@RequestBody UpdateExperimentStatusRequest request, @PathVariable String expId, Authentication auth) {
		log.debug("Received request to update the status of experiment " + expId);
		if (!(expId.equals(request.getExperimentId()))) {
			log.error("Wrong format: experiment ID in URI different than the one in the request");
			return new ResponseEntity<String>("Wrong format: experiment ID in URI different than the one in the request", HttpStatus.BAD_REQUEST);
		}
		
		if(!validateAuthentication(auth)){
			log.warn("Unable to retrieve request authentication information");
			return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
		}
		String user = getUserFromAuth(auth);
		
		try {
			engine.updateExperimentStatus(request, user);
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		} catch (NotExistingEntityException e) {
			log.error("Experiment not found: " + e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (MalformattedElementException e) {
			log.error("Malformatted request: " + e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (WrongStatusException | NotPermittedOperationException e) {
			log.error("Wrong status: " + e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (Exception e) {
			log.error("Internal exception: " + e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@RequestMapping(value = "/experiment/{expId}/timeslot", method = RequestMethod.PUT)
	public ResponseEntity<?> updateExperimentTimeslot(@RequestBody ModifyExperimentTimeslotRequest request, @PathVariable String expId, Authentication auth) {
		log.debug("Received request to modify the proposed timeslot for experiment " + expId);
		if (!(expId.equals(request.getExperimentId()))) {
			log.error("Wrong format: experiment ID in URI different than the one in the request");
			return new ResponseEntity<String>("Wrong format: experiment ID in URI different than the one in the request", HttpStatus.BAD_REQUEST);
		}
		
		if(!validateAuthentication(auth)){
			log.warn("Unable to retrieve request authentication information");
			return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
		}
		String user = getUserFromAuth(auth);
		
		try {
			engine.updateExperimentTimeslot(request, user);
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		} catch (NotExistingEntityException e) {
			log.error("Experiment not found: " + e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (MalformattedElementException e) {
			log.error("Malformatted request: " + e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (WrongStatusException e) {
			log.error("Wrong status: " + e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (Exception e) {
			log.error("Internal exception: " + e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@RequestMapping(value = "/experiment/{expId}/action/{action}", method = RequestMethod.POST)
	public ResponseEntity<?> requestExperimentAction(@RequestBody(required=false) ExecuteExperimentRequest request, @PathVariable String expId, @PathVariable String action, Authentication auth) {
		log.debug("Received request to perform action " + action + " for experiment " + expId);
		if (!(expId.equals(request.getExperimentId()))) {
			log.error("Wrong format: experiment ID in URI different than the one in the request");
			return new ResponseEntity<String>("Wrong format: experiment ID in URI different than the one in the request", HttpStatus.BAD_REQUEST);
		}
		
		if(!validateAuthentication(auth)){
			log.warn("Unable to retrieve request authentication information");
			return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
		}
		String user = getUserFromAuth(auth);
		
		try {
			if (action.equals("deploy")) {
				engine.deployExperiment(expId, user); 
			} else if (action.equals("execute")) {
				if (request == null) {
					log.error("Received null request for experiment execution action");
					return new ResponseEntity<String>("Received null request for experiment execution action", HttpStatus.BAD_REQUEST);
				} else {
					engine.executeExperiment(request, user);
				}
			} else if (action.equals("terminate")) {
				engine.terminateExperiment(expId, user);
			} else if (action.equals("abort")) {
				engine.abortExperiment(expId, user);
			} else {
				log.error("Requested action not supported.");
				return new ResponseEntity<String>("Requested action not supported.", HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>(HttpStatus.ACCEPTED);
		} catch (NotExistingEntityException e) {
			log.error("Experiment not found: " + e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (MalformattedElementException e) {
			log.error("Malformatted request: " + e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (WrongStatusException | NotPermittedOperationException e) {
			log.error("Wrong status: " + e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (Exception e) {
			log.error("Internal exception: " + e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/experiment/{expId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteExperiment(@PathVariable String expId, Authentication auth) {
		log.debug("Received request to terminate experiment " + expId);
		if(!validateAuthentication(auth)){
			log.warn("Unable to retrieve request authentication information");
			return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
		}
		String user = getUserFromAuth(auth);
		
		try {
			engine.purgeExperiment(expId, user);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (MalformattedElementException e) {
			log.error("Malformatted request: " + e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (NotExistingEntityException e) {
			log.error("Experiment not found: " + e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (WrongStatusException | NotPermittedOperationException e) {
			log.error("Wrong status: " + e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (Exception e) {
			log.error("Internal exception: " + e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private Filter buildFilter(String expId, String expDId, String tenantId) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("TENANT_ID", tenantId);
		if ((expId == null) && (expDId == null)) {	}
		else if (expId != null) {
			parameters.put("Exp_ID", expId);
		} else if (expDId != null) {
			parameters.put("ExpD_ID", expDId);
		}
		return new Filter(parameters);
	}


	
}
