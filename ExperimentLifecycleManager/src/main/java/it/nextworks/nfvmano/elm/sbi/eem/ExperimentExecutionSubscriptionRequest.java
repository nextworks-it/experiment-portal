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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public class ExperimentExecutionSubscriptionRequest {

	public enum SubscriptionTypeEnum {
		STATE("EXPERIMENT_EXECUTION_CHANGE_STATE");
		
		private String value;
		
		SubscriptionTypeEnum(String value) {
			this.value = value;
		}
		
		@Override
		@JsonValue
		public String toString() {
			return String.valueOf(value);
		}
		
		@JsonCreator
		public static SubscriptionTypeEnum fromValue(String text) {
			for (SubscriptionTypeEnum b : SubscriptionTypeEnum.values()) {
				if (String.valueOf(b.value).equals(text)) {
					return b;
				}
			}
			return null;
		}
	}
	
	@JsonProperty("subscriptionType")
	private SubscriptionTypeEnum subscriptionType = null;
	
	@JsonProperty("callbackURI")
	private String callbackURI = null;
	
	@JsonProperty("executionId")
	private String executionId = null;

	
	
	public ExperimentExecutionSubscriptionRequest(SubscriptionTypeEnum subscriptionType, String callbackURI,
			String executionId) {
		this.subscriptionType = subscriptionType;
		this.callbackURI = callbackURI;
		this.executionId = executionId;
	}

	public SubscriptionTypeEnum getSubscriptionType() {
		return subscriptionType;
	}

	public String getCallbackURI() {
		return callbackURI;
	}

	public String getExecutionId() {
		return executionId;
	}
	
	
	
}
