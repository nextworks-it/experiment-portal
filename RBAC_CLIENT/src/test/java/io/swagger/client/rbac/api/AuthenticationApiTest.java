/*
 * 5G-EVE RBAC module
 * RBAC module for 5G-EVE portal
 *
 * OpenAPI spec version: 0.0.1
 * Contact: gigarcia@it.uc3m.es
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package io.swagger.client.rbac.api;

import io.swagger.client.rbac.invoker.ApiException;
import io.swagger.client.rbac.model.UserLogin;
import io.swagger.client.rbac.model.UserRegistration;

import org.junit.Test;
import org.junit.Ignore;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * API tests for AuthenticationApi
 */
@Ignore
public class AuthenticationApiTest {

    private final AuthenticationApi api = new AuthenticationApi();

    
    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void loginTest() throws ApiException {
        
        UserLogin body = null;
        
        api.login(body);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void logoutTest() throws ApiException {
        
        api.logout();

        // TODO: test validations
    }
    
    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void registerTest() throws ApiException {
        
        UserRegistration body = null;
        
        api.register(body);

        // TODO: test validations
    }
    
}
