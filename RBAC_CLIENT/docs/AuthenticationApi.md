# AuthenticationApi

All URIs are relative to *https://www.5g-eve.eu/portal/rbac*

Method | HTTP request | Description
------------- | ------------- | -------------
[**login**](AuthenticationApi.md#login) | **POST** /login | 
[**logout**](AuthenticationApi.md#logout) | **GET** /logout | 
[**register**](AuthenticationApi.md#register) | **POST** /register | 




<a name="login"></a>
# **login**
> login(body)



### Example
```java
// Import classes:
//import io.swagger.client.rbac.invoker.ApiException;
//import io.swagger.client.rbac.api.AuthenticationApi;



AuthenticationApi apiInstance = new AuthenticationApi();

UserLogin body = new UserLogin(); // UserLogin | 

try {
    apiInstance.login(body);
} catch (ApiException e) {
    System.err.println("Exception when calling AuthenticationApi#login");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**UserLogin**](UserLogin.md)|  |


### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined


<a name="logout"></a>
# **logout**
> logout()



### Example
```java
// Import classes:
//import io.swagger.client.rbac.invoker.ApiException;
//import io.swagger.client.rbac.api.AuthenticationApi;



AuthenticationApi apiInstance = new AuthenticationApi();

try {
    apiInstance.logout();
} catch (ApiException e) {
    System.err.println("Exception when calling AuthenticationApi#logout");
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
 - **Accept**: Not defined


<a name="register"></a>
# **register**
> register(body)



### Example
```java
// Import classes:
//import io.swagger.client.rbac.invoker.ApiException;
//import io.swagger.client.rbac.api.AuthenticationApi;



AuthenticationApi apiInstance = new AuthenticationApi();

UserRegistration body = new UserRegistration(); // UserRegistration | 

try {
    apiInstance.register(body);
} catch (ApiException e) {
    System.err.println("Exception when calling AuthenticationApi#register");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**UserRegistration**](UserRegistration.md)|  |


### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined



