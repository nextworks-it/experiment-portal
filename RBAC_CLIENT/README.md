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

import io.swagger.client.rbac.invoker.*;
import io.swagger.client.rbac.invoker.auth.*;
import io.swagger.client.rbac.model.*;
import io.swagger.client.rbac.api.AuthenticationApi;

import java.io.File;
import java.util.*;

public class AuthenticationApiExample {

    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        
        

        AuthenticationApi apiInstance = new AuthenticationApi();
        
        UserLogin body = new UserLogin(); // UserLogin | 
        
        try {
            apiInstance.login(body);
        } catch (ApiException e) {
            System.err.println("Exception when calling AuthenticationApi#login");
            e.printStackTrace();
        }
    }
}

```

## Documentation for API Endpoints

All URIs are relative to *https://www.5g-eve.eu/portal/rbac*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*AuthenticationApi* | [**login**](docs/AuthenticationApi.md#login) | **POST** /login | 
*AuthenticationApi* | [**logout**](docs/AuthenticationApi.md#logout) | **GET** /logout | 
*AuthenticationApi* | [**register**](docs/AuthenticationApi.md#register) | **POST** /register | 
*ManagedSitesApi* | [**addManagedSites**](docs/ManagedSitesApi.md#addManagedSites) | **POST** /extra/managed-sites | 
*ManagedSitesApi* | [**deleteManagedSites**](docs/ManagedSitesApi.md#deleteManagedSites) | **DELETE** /extra/managed-sites | 
*ManagedSitesApi* | [**getManagedSites**](docs/ManagedSitesApi.md#getManagedSites) | **GET** /extra/managed-sites | Retrieval of site facilities managed by a specific user
*UseCasesApi* | [**addUseCases**](docs/UseCasesApi.md#addUseCases) | **POST** /extra/use-cases | 
*UseCasesApi* | [**deleteUseCases**](docs/UseCasesApi.md#deleteUseCases) | **DELETE** /extra/use-cases | 
*UseCasesApi* | [**getUseCases**](docs/UseCasesApi.md#getUseCases) | **GET** /extra/use-cases | Obtains use cases asigned to a specific user


## Documentation for Models

 - [ManagedSites](docs/ManagedSites.md)
 - [UseCases](docs/UseCases.md)
 - [UserLogin](docs/UserLogin.md)
 - [UserRegistration](docs/UserRegistration.md)


## Documentation for Authorization

Authentication schemes defined for the API:
### api_key

- **Type**: API key
- **API key parameter name**: Bearer
- **Location**: HTTP header






## Recommendation

It's recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issues.

## Author

gigarcia@it.uc3m.es

