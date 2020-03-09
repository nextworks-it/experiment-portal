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
package it.nextworks.nfvmano.elm.sbi.monitoring;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import it.nextworks.nfvmano.elm.sbi.monitoring.rest.MonitoringRecordItem;
import it.nextworks.nfvmano.elm.sbi.monitoring.rest.MonitoringRecordMessage;
import it.nextworks.nfvmano.elm.sbi.monitoring.rest.MonitoringRecordValueEntry;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.FailedOperationException;

import javax.annotation.PostConstruct;

@Service
public class DataCollectionManagerDriver implements DataCollectionManagerInterface {

	private static final Logger log = LoggerFactory.getLogger(DataCollectionManagerDriver.class);
	
	@Value("${monitoring.type}")
	private String monitoringType;
	
	@Value("${monitoring.address}")
	private String monitoringAddress;
	
	@Value("${monitoring.port}")
	private String monitoringPort;
	
	private RestTemplate restTemplate;
	
	private String targetUrl;
	
	public DataCollectionManagerDriver() {

	}


    @PostConstruct
    private void setupClient() {
        this.targetUrl = "http://" + monitoringAddress + ":" + monitoringPort + "/dcm/publish/";
        this.restTemplate= new RestTemplate(new BufferingClientHttpRequestFactory(
                new SimpleClientHttpRequestFactory()
        ));
        log.debug("DCM driver client set. Target URL:"+targetUrl);
    }

	@Override
	public void subscribe(List<MonitoringDataItem> item, MonitoringDataType mdt) throws FailedOperationException {
		log.debug("Subscribing for monitoring of " + mdt.toString().toLowerCase());

		if (monitoringType.equalsIgnoreCase("DUMMY")) {
			log.debug("DCM configured in dummy mode. Message not really sent");
			return;
		}
		
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "application/json");
		
		String url = buildUrl(mdt);
		
		List<MonitoringRecordItem> records = new ArrayList<MonitoringRecordItem>();
		for (MonitoringDataItem mdi : item) {
		    log.debug("Monitoring item:" + mdi.getDataItemString());
			MonitoringRecordItem mri = new MonitoringRecordItem(new MonitoringRecordValueEntry(mdi.getDataItemString(), mdi.getExpId(), "subscribe"));
			records.add(mri);
		}
		MonitoringRecordMessage message = new MonitoringRecordMessage(records);
		
		HttpEntity<?> postEntity = new HttpEntity<>(message, header);
		
		try {
			ResponseEntity<String> httpResponse =
					restTemplate.exchange(url, HttpMethod.POST, postEntity, String.class);
			
			log.debug("Response code: " + httpResponse.getStatusCode().toString());
						
		} catch (Exception e) {
			log.debug("Error while interacting with DCM."+e.getMessage());
			throw new FailedOperationException("Error while interacting with DCM: " + e.getMessage());
		}
	}

	@Override
	public void unsubscribe(List<MonitoringDataItem> item, MonitoringDataType mdt) throws FailedOperationException {
		log.debug("Unsubscribing for monitoring of " + mdt.toString().toLowerCase());

		if (monitoringType.equalsIgnoreCase("DUMMY")) {
			log.debug("DCM configured in dummy mode. Message not really sent");
			return;
		}
		
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "application/json");
		
		String url = buildUrl(mdt);
		
		List<MonitoringRecordItem> records = new ArrayList<MonitoringRecordItem>();
		for (MonitoringDataItem mdi : item) {
			MonitoringRecordItem mri = new MonitoringRecordItem(new MonitoringRecordValueEntry(mdi.getDataItemString(), mdi.getExpId(), "unsubscribe"));
			records.add(mri);
		}
		MonitoringRecordMessage message = new MonitoringRecordMessage(records);
        ObjectMapper objMapper = new ObjectMapper();
		HttpEntity<?> postEntity = new HttpEntity<>(message, header);
		
		try {
			ResponseEntity<String> httpResponse =
					restTemplate.exchange(url, HttpMethod.POST, postEntity, String.class);
			
			log.debug("Response code: " + httpResponse.getStatusCode().toString());
						
		} catch (Exception e) {
			log.debug("Error while interacting with DCM.");
			throw new FailedOperationException("Error while interacting with DCM: " + e.getMessage());
		}
	}
	
	private String buildUrl(MonitoringDataType mdt) {
		switch (mdt) {
		case APPLICATION_METRIC:
			return (targetUrl + "signalling.metric.application");
			
		case INFRASTRUCTURE_METRIC:
			return (targetUrl + "signalling.metric.infrastructure");

		case KPI:
			return (targetUrl + "signalling.kpi"); 
		
		case RESULT:
			return (targetUrl + "signalling.result"); 

		default:
			return null;
		}
	}

}
