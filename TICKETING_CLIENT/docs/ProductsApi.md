# ProductsApi

All URIs are relative to *https://www.5g-eve.eu/portal/tsb*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getProducts**](ProductsApi.md#getProducts) | **GET** /products | Finds available products that are susceptible for ticket association


<a name="getProducts"></a>
# **getProducts**
> getProducts()

Finds available products that are susceptible for ticket association



### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ProductsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: api_key
ApiKeyAuth api_key = (ApiKeyAuth) defaultClient.getAuthentication("api_key");
api_key.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//api_key.setApiKeyPrefix("Token");

ProductsApi apiInstance = new ProductsApi();
try {
    apiInstance.getProducts();
} catch (ApiException e) {
    System.err.println("Exception when calling ProductsApi#getProducts");
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

