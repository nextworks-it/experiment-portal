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
package it.nextworks.nfvmano.elm.engine.messages;

public enum InternalMessageType {

	SCHEDULE_REQUEST,
	UPDATE_STATE_REQUEST,
	DEPLOY_EXPERIMENT_REQUEST,
	NFV_NS_STATUS_CHANGE_NOTIFICATION,
	EXECUTE_EXPERIMENT_REQUEST,
	EEM_NOTIFICATION,
	TERMINATE_EXPERIMENT_REQUEST
	
}
