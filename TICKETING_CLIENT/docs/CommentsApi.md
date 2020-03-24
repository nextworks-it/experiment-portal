# CommentsApi

All URIs are relative to *https://www.5g-eve.eu/portal/tsb*

Method | HTTP request | Description
------------- | ------------- | -------------
[**ticketsTicketIdCommentsGet**](CommentsApi.md#ticketsTicketIdCommentsGet) | **GET** /tickets/{ticket_id}/comments | Retrieve comments of a ticket
[**ticketsTicketIdCommentsPost**](CommentsApi.md#ticketsTicketIdCommentsPost) | **POST** /tickets/{ticket_id}/comments | Creates a new comment for a ticket
[**ticketsTicketIdCommentsTrustedPost**](CommentsApi.md#ticketsTicketIdCommentsTrustedPost) | **POST** /tickets/{ticket_id}/comments/trusted | Creates a new comment for a ticket from a trusted service


<a name="ticketsTicketIdCommentsGet"></a>
# **ticketsTicketIdCommentsGet**
> ticketsTicketIdCommentsGet(ticketId)

Retrieve comments of a ticket

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.CommentsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: api_key
ApiKeyAuth api_key = (ApiKeyAuth) defaultClient.getAuthentication("api_key");
api_key.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//api_key.setApiKeyPrefix("Token");

CommentsApi apiInstance = new CommentsApi();
String ticketId = "ticketId_example"; // String | The Ticket ID
try {
    apiInstance.ticketsTicketIdCommentsGet(ticketId);
} catch (ApiException e) {
    System.err.println("Exception when calling CommentsApi#ticketsTicketIdCommentsGet");
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

<a name="ticketsTicketIdCommentsPost"></a>
# **ticketsTicketIdCommentsPost**
> ticketsTicketIdCommentsPost(body)

Creates a new comment for a ticket

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.CommentsApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure API key authorization: api_key
ApiKeyAuth api_key = (ApiKeyAuth) defaultClient.getAuthentication("api_key");
api_key.setApiKey("YOUR API KEY");
// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)
//api_key.setApiKeyPrefix("Token");

CommentsApi apiInstance = new CommentsApi();
NewComment body = new NewComment(); // NewComment | New comment to be created
try {
    apiInstance.ticketsTicketIdCommentsPost(body);
} catch (ApiException e) {
    System.err.println("Exception when calling CommentsApi#ticketsTicketIdCommentsPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**NewComment**](NewComment.md)| New comment to be created |

### Return type

null (empty response body)

### Authorization

[api_key](../README.md#api_key)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="ticketsTicketIdCommentsTrustedPost"></a>
# **ticketsTicketIdCommentsTrustedPost**
> ticketsTicketIdCommentsTrustedPost(body)

Creates a new comment for a ticket from a trusted service

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.CommentsApi;


CommentsApi apiInstance = new CommentsApi();
NewTrustedComment body = new NewTrustedComment(); // NewTrustedComment | New comment to be created
try {
    apiInstance.ticketsTicketIdCommentsTrustedPost(body);
} catch (ApiException e) {
    System.err.println("Exception when calling CommentsApi#ticketsTicketIdCommentsTrustedPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**NewTrustedComment**](NewTrustedComment.md)| New comment to be created |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

