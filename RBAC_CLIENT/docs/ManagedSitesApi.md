# ManagedSitesApi

All URIs are relative to *https://www.5g-eve.eu/portal/rbac*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addManagedSites**](ManagedSitesApi.md#addManagedSites) | **POST** /extra/managed-sites | 
[**deleteManagedSites**](ManagedSitesApi.md#deleteManagedSites) | **DELETE** /extra/managed-sites | 
[**getManagedSites**](ManagedSitesApi.md#getManagedSites) | **GET** /extra/managed-sites | Retrieval of site facilities managed by a specific user




<a name="addManagedSites"></a>
# **addManagedSites**
> addManagedSites(body)



Appends new site facilities to a specific user (being the real site manager)

### Example
```java
// Import classes:
//import io.swagger.client.rbac.invoker.ApiException;
//import io.swagger.client.rbac.api.ManagedSitesApi;



ManagedSitesApi apiInstance = new ManagedSitesApi();

ManagedSites body = new ManagedSites(); // ManagedSites | 

try {
    apiInstance.addManagedSites(body);
} catch (ApiException e) {
    System.err.println("Exception when calling ManagedSitesApi#addManagedSites");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**ManagedSites**](ManagedSites.md)|  |


### Return type

null (empty response body)

### Authorization

[api_key](../README.md#api_key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined


<a name="deleteManagedSites"></a>
# **deleteManagedSites**
> deleteManagedSites(body)



Removes assigned sites to a specific user

### Example
```java
// Import classes:
//import io.swagger.client.rbac.invoker.ApiException;
//import io.swagger.client.rbac.api.ManagedSitesApi;



ManagedSitesApi apiInstance = new ManagedSitesApi();

ManagedSites body = new ManagedSites(); // ManagedSites | 

try {
    apiInstance.deleteManagedSites(body);
} catch (ApiException e) {
    System.err.println("Exception when calling ManagedSitesApi#deleteManagedSites");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**ManagedSites**](ManagedSites.md)|  |


### Return type

null (empty response body)

### Authorization

[api_key](../README.md#api_key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined


<a name="getManagedSites"></a>
# **getManagedSites**
> getManagedSites(body)

Retrieval of site facilities managed by a specific user

### Example
```java
// Import classes:
//import io.swagger.client.rbac.invoker.ApiException;
//import io.swagger.client.rbac.api.ManagedSitesApi;



ManagedSitesApi apiInstance = new ManagedSitesApi();

ManagedSites body = new ManagedSites(); // ManagedSites | 

try {
    apiInstance.getManagedSites(body);
} catch (ApiException e) {
    System.err.println("Exception when calling ManagedSitesApi#getManagedSites");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**ManagedSites**](ManagedSites.md)|  |


### Return type

null (empty response body)

### Authorization

[api_key](../README.md#api_key)

### HTTP request headers

 - **Content-Type**: */*
 - **Accept**: Not defined



