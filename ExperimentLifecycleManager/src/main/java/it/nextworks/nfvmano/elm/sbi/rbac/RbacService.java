package it.nextworks.nfvmano.elm.sbi.rbac;

import io.swagger.client.rbac.api.ManagedSitesApi;
import io.swagger.client.rbac.invoker.ApiClient;
import io.swagger.client.rbac.model.ManagedSites;
import it.nextworks.nfvmano.catalogue.blueprint.elements.EveSite;
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

    public List<EveSite> getUserManagedSites() {
        log.debug("Retrieving user sites");

        ManagedSites managedSites = managedSitesApi.getManagedSites(new ManagedSites());
        List<EveSite> siteList = new ArrayList<>();
        for(String site: managedSites.getManagedSites()){
            siteList.add(EveSite.valueOf(site));

        }
        return siteList;

    }


}
