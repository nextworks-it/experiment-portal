# swagger-java-client

## Requirements

Building the API client library requires [Maven](https://maven.apache.org/) to be installed.

## Installation

To install the API client library to your local Maven repository, simply execute:

```shell
mvn install
```

To deploy it to a remote Maven repository instead, configure the settings of the repository and execute:

```shell
mvn deploy
```

Refer to the [official documentation](https://maven.apache.org/plugins/maven-deploy-plugin/usage.html) for more information.

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
    <groupId>io.swagger</groupId>
    <artifactId>swagger-java-client</artifactId>
    <version>1.0.0</version>
    <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
compile "io.swagger:swagger-java-client:1.0.0"
```

### Others

At first generate the JAR by executing:

    mvn package

Then manually install the following JARs:

* target/swagger-java-client-1.0.0.jar
* target/lib/*.jar

## Getting Started

Please follow the [installation](#installation) instruction and execute the following Java code:

```java

import io.swagger.client.*;
import io.swagger.client.auth.*;
import io.swagger.client.model.*;
import io.swagger.client.api.CommentsApi;

import java.io.File;
import java.util.*;

public class CommentsApiExample {

    public static void main(String[] args) {
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
    }
}

```

## Documentation for API Endpoints

All URIs are relative to *https://www.5g-eve.eu/portal/tsb*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*CommentsApi* | [**ticketsTicketIdCommentsGet**](docs/CommentsApi.md#ticketsTicketIdCommentsGet) | **GET** /tickets/{ticket_id}/comments | Retrieve comments of a ticket
*CommentsApi* | [**ticketsTicketIdCommentsPost**](docs/CommentsApi.md#ticketsTicketIdCommentsPost) | **POST** /tickets/{ticket_id}/comments | Creates a new comment for a ticket
*CommentsApi* | [**ticketsTicketIdCommentsTrustedPost**](docs/CommentsApi.md#ticketsTicketIdCommentsTrustedPost) | **POST** /tickets/{ticket_id}/comments/trusted | Creates a new comment for a ticket from a trusted service
*ComponentsApi* | [**getComponents**](docs/ComponentsApi.md#getComponents) | **GET** /components | Finds available components that are susceptible for ticket association
*ProductsApi* | [**getProducts**](docs/ProductsApi.md#getProducts) | **GET** /products | Finds available products that are susceptible for ticket association
*SiteAdminUsersApi* | [**getAdminUsers**](docs/SiteAdminUsersApi.md#getAdminUsers) | **GET** /users | Finds admin users to whom we can assign tickets
*TicketsApi* | [**addTicket**](docs/TicketsApi.md#addTicket) | **POST** /tickets | Add a new ticket
*TicketsApi* | [**addTrustedTicket**](docs/TicketsApi.md#addTrustedTicket) | **POST** /tickets/trusted | Add a new ticket from a trusted service
*TicketsApi* | [**getTickets**](docs/TicketsApi.md#getTickets) | **GET** /tickets | Finds tickets assotiated to a specific user
*TicketsApi* | [**ticketsTicketIdGet**](docs/TicketsApi.md#ticketsTicketIdGet) | **GET** /tickets/{ticket_id} | Retrieve a specific ticket


## Documentation for Models

 - [NewComment](docs/NewComment.md)
 - [NewTicket](docs/NewTicket.md)
 - [NewTrustedComment](docs/NewTrustedComment.md)
 - [NewTrustedTicket](docs/NewTrustedTicket.md)


## Documentation for Authorization

Authentication schemes defined for the API:
### api_key

- **Type**: API key
- **API key parameter name**: Bearer
- **Location**: HTTP header


## Recommendation

It's recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issue.

## Author

gigarcia@it.uc3m.es

