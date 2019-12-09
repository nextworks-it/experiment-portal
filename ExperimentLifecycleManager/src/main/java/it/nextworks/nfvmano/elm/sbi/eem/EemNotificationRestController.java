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
package it.nextworks.nfvmano.elm.sbi.eem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import it.nextworks.nfvmano.elm.engine.ExperimentLifecycleManagerEngine;
import it.nextworks.nfvmano.elm.sbi.eem.messages.ExperimentExecutionStatusChangeNotification;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MalformattedElementException;


@RestController
@CrossOrigin
@RequestMapping("/elm/notification")

//elm/notification/eem
public class EemNotificationRestController {

	private static final Logger log = LoggerFactory.getLogger(EemNotificationRestController.class);
	
	@Autowired
	private ExperimentLifecycleManagerEngine engine;
	
	public EemNotificationRestController() { }

	@RequestMapping(value = "/eem", method = RequestMethod.POST)
	public ResponseEntity<?> notifyFromEem(@RequestBody ExperimentExecutionStatusChangeNotification notification) {
		log.debug("Received notification from EEM");
		try {
			notification.isValid();
			engine.notifyExperimentExecutionStatusChange(notification);
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (MalformattedElementException e) {
			log.error("Received malformed notification");
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
