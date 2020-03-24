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
package it.nextworks.nfvmano.elm.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.nextworks.nfvmano.elm.im.Experiment;

public interface ExperimentRepository extends JpaRepository<Experiment, Long> {

	Optional<Experiment> findByExperimentId(String experimentId);
	
	List<Experiment> findByExperimentDescriptorId(String expDId);
	
	Optional<Experiment> findByNfvNsInstanceId(String nsInstanceId);
	
	List<Experiment> findByTenantId(String tenantId);
	
	List<Experiment> findByTenantIdAndExperimentDescriptorId(String tenantId, String expDId);
	
	Optional<Experiment> findByTenantIdAndExperimentId(String tenantId, String experimentId);

	List<Experiment> findAll();
}
