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

import javax.persistence.Embeddable;

import it.nextworks.nfvmano.catalogue.blueprint.elements.EveSite;
import it.nextworks.nfvmano.catalogue.blueprint.elements.MetricCollectionType;
import it.nextworks.nfvmano.catalogue.blueprint.elements.MetricGraphType;

@Embeddable
public class MonitoringDataItem {

	private String expId;
	private MonitoringDataType mdt;
	private EveSite site;

	//This is filled the metricId or the kpi id
	private String mdName;
	private String useCase;

	//added to support DCM context
	private String metricName;
	private MetricGraphType metricGraphType;
	private MetricCollectionType metricCollectionType;
	private String metricUnit;
	private String metricInterval;

	public MonitoringDataItem() { }
	
	public MonitoringDataItem(String expId,
			MonitoringDataType mdt,
			EveSite site,
			String mdName,
		    	String metricName,
			MetricGraphType metricGraphType,
			MetricCollectionType metricCollectionType,
		   	String metricUnit,
			String metricInterval,
			String useCase
		  ) {
		this.expId = expId;
		this.mdName = mdName;
		this.site = site;
		this.mdt = mdt;
		this.metricName = metricName;
		this.metricGraphType = metricGraphType;
		this.metricCollectionType = metricCollectionType;
		this.metricUnit = metricUnit;
		this.metricInterval= metricInterval;
		this.useCase = useCase;
	}

	/**
	 * @return the experimentId
	 */
	public String getExpId() {
		return expId;
	}

	/**
	 * @return the mdt
	 */
	public MonitoringDataType getMdt() {
		return mdt;
	}

	/**
	 * @return the site
	 */
	public EveSite getSite() {
		return site;
	}

	/**
	 * @return the mdName
	 */
	public String getMdName() {
		return mdName;
	}
	
	public String getDataItemString() {
		String s = useCase+"."
				+ expId + "."
				+ site.toString().toLowerCase() + "." 
				+ mdt.toString().toLowerCase() + "." 
				+ mdName;
		return s;
	}


	public String getMetricName() {
		return metricName;
	}

	public MetricGraphType getMetricGraphType() {
		return metricGraphType;
	}

	public MetricCollectionType getMetricCollectionType() {
		return metricCollectionType;
	}

	public String getMetricUnit() {
		return metricUnit;
	}

	public String getMetricInterval() {
		return metricInterval;
	}
}
