package it.nextworks.nfvmano.elm.engine.messages;

import com.fasterxml.jackson.annotation.JsonCreator;

public class TerminateExperimentInternalMessage extends InternalMessage {

	@JsonCreator
	public TerminateExperimentInternalMessage() {
		this.type = InternalMessageType.TERMINATE_EXPERIMENT_REQUEST;
	}

}
