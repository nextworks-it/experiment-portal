package it.nextworks.nfvmano.elm.sbi.rbac;

import io.swagger.client.rbac.api.ManagedSitesApi;
import io.swagger.client.rbac.invoker.ApiClient;
import io.swagger.client.rbac.model.ManagedSites;
import it.nextworks.nfvmano.catalogue.blueprint.elements.EveSite;
import it.nextworks.nfvmano.libs.ifa.common.exceptions.MalformattedElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class RbacService {
    private static final Logger log = LoggerFactory.getLogger(RbacService.class);
    @Autowired
    private RestTemplate restTemplate;
    private ManagedSitesApi managedSitesApi;

    @Value("${rbac.url}")
    private String rbacUrl;

    @PostConstruct
    private void initRbacClients(){
        log.debug("Creating RBAC clients");
        ApiClient apiClient = new ApiClient(restTemplate);
        apiClient.setBasePath(rbacUrl);
        this.managedSitesApi = new ManagedSitesApi(apiClient);

    }

    public List<EveSite> getUserManagedSites() throws MalformattedElementException {
        log.debug("Retrieving user sites");

        ManagedSites managedSites = managedSitesApi.getManagedSites(new ManagedSites());
        List<EveSite> siteList = new ArrayList<>();
        for(String site: managedSites.getManagedSites()){
            try{
                EveSite currentSite = EveSite.valueOf(site);
                siteList.add(currentSite);
            }catch (IllegalArgumentException e){
                log.error("Error retrieving user manager sites for site:"+site, e);
                throw new MalformattedElementException("Site administrator sites not correctly configured (invalid site: "+site+"), please contact the system administrator");

            }
        }
        log.debug("Retrieved user sites:"+siteList);
        return siteList;

    }


}
