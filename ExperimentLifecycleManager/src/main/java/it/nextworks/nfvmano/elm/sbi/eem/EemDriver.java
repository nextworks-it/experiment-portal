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

import it.nextworks.nfvmano.elm.im.ExperimentExecution;
import it.nextworks.nfvmano.elm.sbi.eem.interfaces.EemConsumerInterface;
import it.nextworks.nfvmano.elm.sbi.eem.messages.RunExecutionRequest;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MalformattedElementException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MethodNotImplementedException;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.NotExistingEntityException;

public class EemDriver extends EemAbstractDriver {

	private String noficationUrl;
	
	public EemDriver(String eemAddress, int eemPort, String noficationUrl) {
		super(EemDriverType.EEM, null);
		this.noficationUrl = noficationUrl;
		this.eemUrl = "http://" + eemAddress + ":" + eemPort + "/eem/";
	}

	@Override
	public String createExperimentExecutionInstance() throws MethodNotImplementedException, FailedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void runExperimentExecution(RunExecutionRequest request)
			throws MethodNotImplementedException, FailedOperationException, NotExistingEntityException, MalformattedElementException {
		// TODO Auto-generated method stub

	}

	@Override
	public ExperimentExecution getExperimentExecution(String experimentExecutionId)
			throws MethodNotImplementedException, FailedOperationException, NotExistingEntityException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void abortExperimentExecution(String experimentExecutionId)
			throws MethodNotImplementedException, FailedOperationException, NotExistingEntityException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeExperimentExecutionRecord(String experimentExecutionId)
			throws MethodNotImplementedException, FailedOperationException, NotExistingEntityException {
		// TODO Auto-generated method stub

	}

	@Override
	public String subscribe(String experimentExecutionId, EemConsumerInterface consumer)
			throws MethodNotImplementedException, FailedOperationException, NotExistingEntityException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unsubscribe(String subscriptionId)
			throws MethodNotImplementedException, FailedOperationException, NotExistingEntityException {
		// TODO Auto-generated method stub

	}

}
