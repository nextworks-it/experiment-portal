# UseCasesApi

All URIs are relative to *https://www.5g-eve.eu/portal/rbac*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addUseCases**](UseCasesApi.md#addUseCases) | **POST** /extra/use-cases | 
[**deleteUseCases**](UseCasesApi.md#deleteUseCases) | **DELETE** /extra/use-cases | 
[**getUseCases**](UseCasesApi.md#getUseCases) | **GET** /extra/use-cases | Obtains use cases asigned to a specific user




<a name="addUseCases"></a>
# **addUseCases**
> addUseCases(body)



Appends new use cases to a specific user

### Example
```java
// Import classes:
//import io.swagger.client.rbac.invoker.ApiException;
//import io.swagger.client.rbac.api.UseCasesApi;



UseCasesApi apiInstance = new UseCasesApi();

UseCases body = new UseCases(); // UseCases | 

try {
    apiInstance.addUseCases(body);
} catch (ApiException e) {
    System.err.println("Exception when calling UseCasesApi#addUseCases");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**UseCases**](UseCases.md)|  |


### Return type

null (empty response body)

### Authorization

[api_key](../README.md#api_key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined


<a name="deleteUseCases"></a>
# **deleteUseCases**
> deleteUseCases(body)



Removes assigned use cases to a specific user

### Example
```java
// Import classes:
//import io.swagger.client.rbac.invoker.ApiException;
//import io.swagger.client.rbac.api.UseCasesApi;



UseCasesApi apiInstance = new UseCasesApi();

UseCases body = new UseCases(); // UseCases | 

try {
    apiInstance.deleteUseCases(body);
} catch (ApiException e) {
    System.err.println("Exception when calling UseCasesApi#deleteUseCases");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**UseCases**](UseCases.md)|  |


### Return type

null (empty response body)

### Authorization

[api_key](../README.md#api_key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined


<a name="getUseCases"></a>
# **getUseCases**
> getUseCases(body)

Obtains use cases asigned to a specific user

### Example
```java
// Import classes:
//import io.swagger.client.rbac.invoker.ApiException;
//import io.swagger.client.rbac.api.UseCasesApi;



UseCasesApi apiInstance = new UseCasesApi();

UseCases body = new UseCases(); // UseCases | 

try {
    apiInstance.getUseCases(body);
} catch (ApiException e) {
    System.err.println("Exception when calling UseCasesApi#getUseCases");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**UseCases**](UseCases.md)|  |


### Return type

null (empty response body)

### Authorization

[api_key](../README.md#api_key)

### HTTP request headers

 - **Content-Type**: */*
 - **Accept**: Not defined



