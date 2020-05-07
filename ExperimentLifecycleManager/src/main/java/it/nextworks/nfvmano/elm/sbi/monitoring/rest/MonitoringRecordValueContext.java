package it.nextworks.nfvmano.elm.sbi.monitoring.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.nextworks.nfvmano.catalogue.blueprint.elements.MetricCollectionType;
import it.nextworks.nfvmano.catalogue.blueprint.elements.MetricGraphType;

public class MonitoringRecordValueContext {


    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String metricId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String kpiId;

    //should be null for KPIs
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MetricCollectionType metricCollectionType;
    private MetricGraphType graph;

    private String name;
    private String unit;
    private String interval;

    public MonitoringRecordValueContext(String metricId, String kpiId, MetricCollectionType metricCollectionType, MetricGraphType graph, String name, String unit, String interval) {
        this.metricId = metricId;
        this.kpiId = kpiId;
        this.metricCollectionType = metricCollectionType;
        this.graph = graph;
        this.name = name;
        this.unit = unit;
        this.interval = interval;
    }

    public String getMetricId() {
        return metricId;
    }

    public String getKpiId() {
        return kpiId;
    }

    public MetricCollectionType getMetricCollectionType() {
        return metricCollectionType;
    }

    public MetricGraphType getGraph() {
        return graph;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public String getInterval() {
        return interval;
    }
}


