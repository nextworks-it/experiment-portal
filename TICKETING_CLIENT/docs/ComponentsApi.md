# ComponentsApi

All URIs are relative to *https://www.5g-eve.eu/portal/tsb*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getComponents**](ComponentsApi.md#getComponents) | **GET** /components | Finds available components that are susceptible for ticket association


<a name="getComponents"></a>
# **getComponents**
> getComponents()

Finds available components that are susceptible for ticket association



### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ComponentsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: api_key
ApiKeyAuth api_key = (ApiKeyAuth) defaultClient.getAuthentication("api_key");
api_key.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//api_key.setApiKeyPrefix("Token");

ComponentsApi apiInstance = new ComponentsApi();
try {
    apiInstance.getComponents();
} catch (ApiException e) {
    System.err.println("Exception when calling ComponentsApi#getComponents");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

null (empty response body)

### Authorization

[api_key](../README.md#api_key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

