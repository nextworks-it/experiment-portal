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

import javax.persistence.Embeddable;

@Embeddable
public class ExecutionResult {

	private String testCaseName;
	private ExperimentExecutionResultCode resultCode;
	private String result;
	
	public ExecutionResult() {	}
	
	public ExecutionResult(String testCaseName,
			ExperimentExecutionResultCode resultCode,
			String result) {
		this.testCaseName = testCaseName;
		this.resultCode = resultCode;
		this.result = result;
	}
	
	

	/**
	 * @return the testCaseName
	 */
	public String getTestCaseName() {
		return testCaseName;
	}

	/**
	 * @return the resultCode
	 */
	public ExperimentExecutionResultCode getResultCode() {
		return resultCode;
	}

	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	
	
}
