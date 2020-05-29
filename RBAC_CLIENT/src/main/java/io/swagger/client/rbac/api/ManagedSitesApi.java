package io.swagger.client.rbac.api;

import io.swagger.client.rbac.invoker.ApiClient;

import io.swagger.client.rbac.model.ManagedSites;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.java.JavaClientCodegen", date = "2020-05-26T18:08:24.917+02:00[Europe/Rome]")
@Component("io.swagger.client.rbac.api.ManagedSitesApi")

public class ManagedSitesApi {
    private ApiClient apiClient;

    public ManagedSitesApi() {
        this(new ApiClient());
    }

    @Autowired
    public ManagedSitesApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    
    /**
     * 
     * Appends new site facilities to a specific user (being the real site manager)
     * <p><b>204</b>
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * @param body The body parameter
     * @throws RestClientException if an error occurs while attempting to invoke the API

     */
    public void addManagedSites(ManagedSites body) throws RestClientException {
        Object postBody = body;
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling addManagedSites");
        }
        
        String path = UriComponentsBuilder.fromPath("/extra/managed-sites").build().toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] accepts = {  };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "api_key" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        apiClient.invokeAPI(path, HttpMethod.POST, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    
    /**
     * 
     * Removes assigned sites to a specific user
     * <p><b>204</b>
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * @param body The body parameter
     * @throws RestClientException if an error occurs while attempting to invoke the API

     */
    public void deleteManagedSites(ManagedSites body) throws RestClientException {
        Object postBody = body;
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling deleteManagedSites");
        }
        
        String path = UriComponentsBuilder.fromPath("/extra/managed-sites").build().toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] accepts = {  };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "api_key" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        apiClient.invokeAPI(path, HttpMethod.DELETE, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    
    /**
     * Retrieval of site facilities managed by a specific user
     * 
     * <p><b>200</b> - List of managed sites assotiated to a specific user
     * <p><b>400</b> - Bad request
     * <p><b>401</b> - Unauthorized
     * @param body The body parameter
     * @throws RestClientException if an error occurs while attempting to invoke the API

     */
    public ManagedSites getManagedSites(ManagedSites body) throws RestClientException {
        Object postBody = body;
        
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'body' when calling getManagedSites");
        }
        
        String path = UriComponentsBuilder.fromPath("/extra/managed-sites").build().toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] accepts = {  };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = { 
            "application/json"
         };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "api_key" };

        ParameterizedTypeReference<ManagedSites> returnType = new ParameterizedTypeReference<ManagedSites>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);

    }
    
}

