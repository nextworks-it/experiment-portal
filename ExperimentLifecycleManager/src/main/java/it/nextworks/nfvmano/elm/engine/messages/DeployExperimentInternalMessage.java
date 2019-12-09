package it.nextworks.nfvmano.elm.engine.messages;

import com.fasterxml.jackson.annotation.JsonCreator;

public class DeployExperimentInternalMessage extends InternalMessage {

	@JsonCreator
	public DeployExperimentInternalMessage() {
		this.type = InternalMessageType.DEPLOY_EXPERIMENT_REQUEST;
	}

}
