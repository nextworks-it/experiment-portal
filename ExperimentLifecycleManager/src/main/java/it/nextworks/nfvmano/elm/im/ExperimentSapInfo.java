package it.nextworks.nfvmano.elm.im;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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
@Entity
public class ExperimentSapInfo {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @JsonIgnore
    @ManyToOne
    private Experiment experiment;

    private String sapdId;
    private String sapName;
    private String description;
    private String address;


    public ExperimentSapInfo() {
    }

    public ExperimentSapInfo(Experiment experiment, String sapdId, String sapName, String description, String address) {
        this.experiment = experiment;
        this.sapdId = sapdId;
        this.sapName = sapName;
        this.description = description;
        this.address = address;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public String getSapdId() {
        return sapdId;
    }

    public String getSapName() {
        return sapName;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }
}
