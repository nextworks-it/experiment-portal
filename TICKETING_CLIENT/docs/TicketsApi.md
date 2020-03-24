# TicketsApi

All URIs are relative to *https://www.5g-eve.eu/portal/tsb*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addTicket**](TicketsApi.md#addTicket) | **POST** /tickets | Add a new ticket
[**addTrustedTicket**](TicketsApi.md#addTrustedTicket) | **POST** /tickets/trusted | Add a new ticket from a trusted service
[**getTickets**](TicketsApi.md#getTickets) | **GET** /tickets | Finds tickets assotiated to a specific user
[**ticketsTicketIdGet**](TicketsApi.md#ticketsTicketIdGet) | **GET** /tickets/{ticket_id} | Retrieve a specific ticket


<a name="addTicket"></a>
# **addTicket**
> addTicket(body)

Add a new ticket



### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.TicketsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: api_key
ApiKeyAuth api_key = (ApiKeyAuth) defaultClient.getAuthentication("api_key");
api_key.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//api_key.setApiKeyPrefix("Token");

TicketsApi apiInstance = new TicketsApi();
NewTicket body = new NewTicket(); // NewTicket | New ticket to be created
try {
    apiInstance.addTicket(body);
} catch (ApiException e) {
    System.err.println("Exception when calling TicketsApi#addTicket");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**NewTicket**](NewTicket.md)| New ticket to be created |

### Return type

null (empty response body)

### Authorization

[api_key](../README.md#api_key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="addTrustedTicket"></a>
# **addTrustedTicket**
> addTrustedTicket(body)

Add a new ticket from a trusted service



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.TicketsApi;


TicketsApi apiInstance = new TicketsApi();
NewTrustedTicket body = new NewTrustedTicket(); // NewTrustedTicket | New ticket to be created
try {
    apiInstance.addTrustedTicket(body);
} catch (ApiException e) {
    System.err.println("Exception when calling TicketsApi#addTrustedTicket");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**NewTrustedTicket**](NewTrustedTicket.md)| New ticket to be created |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getTickets"></a>
# **getTickets**
> getTickets()

Finds tickets assotiated to a specific user



### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.TicketsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: api_key
ApiKeyAuth api_key = (ApiKeyAuth) defaultClient.getAuthentication("api_key");
api_key.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//api_key.setApiKeyPrefix("Token");

TicketsApi apiInstance = new TicketsApi();
try {
    apiInstance.getTickets();
} catch (ApiException e) {
    System.err.println("Exception when calling TicketsApi#getTickets");
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

<a name="ticketsTicketIdGet"></a>
# **ticketsTicketIdGet**
> ticketsTicketIdGet(ticketId)

Retrieve a specific ticket

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.TicketsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: api_key
ApiKeyAuth api_key = (ApiKeyAuth) defaultClient.getAuthentication("api_key");
api_key.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//api_key.setApiKeyPrefix("Token");

TicketsApi apiInstance = new TicketsApi();
String ticketId = "ticketId_example"; // String | The Ticket ID
try {
    apiInstance.ticketsTicketIdGet(ticketId);
} catch (ApiException e) {
    System.err.println("Exception when calling TicketsApi#ticketsTicketIdGet");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **ticketId** | **String**| The Ticket ID |

### Return type

null (empty response body)

### Authorization

[api_key](../README.md#api_key)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

