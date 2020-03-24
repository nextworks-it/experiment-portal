# SiteAdminUsersApi

All URIs are relative to *https://www.5g-eve.eu/portal/tsb*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getAdminUsers**](SiteAdminUsersApi.md#getAdminUsers) | **GET** /users | Finds admin users to whom we can assign tickets


<a name="getAdminUsers"></a>
# **getAdminUsers**
> getAdminUsers()

Finds admin users to whom we can assign tickets



### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.SiteAdminUsersApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: api_key
ApiKeyAuth api_key = (ApiKeyAuth) defaultClient.getAuthentication("api_key");
api_key.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//api_key.setApiKeyPrefix("Token");

SiteAdminUsersApi apiInstance = new SiteAdminUsersApi();
try {
    apiInstance.getAdminUsers();
} catch (ApiException e) {
    System.err.println("Exception when calling SiteAdminUsersApi#getAdminUsers");
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

