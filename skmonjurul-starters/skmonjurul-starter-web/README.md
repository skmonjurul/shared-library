# Custom Spring Boot Starter for spring boot starter web

## How to use
### Add this starter as a dependency in your pom.xml file of your spring boot client application

```
<dependency>
    <groupId>com.skmonjurul.shared-library</groupId>
    <artifactId>skmonjurul-starter-web</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## Auto configuration
* [RestTemplate](#resttemplate)
* [Health Information](#health-information)

## RestTemplate
Add this library to your spring boot application pom.xml file. This library will provide a `RestTemplate` bean with 
some configuration. Once you add this library to your spring boot application, you can inject `RestTemplate` bean in your
application.

```java
package com.skmonjurul.product_service.service;

import com.skmonjurul.product_service.exception.ResourceNotFoundException;
import com.skmonjurul.product_service.openapi.model.Product;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;


@Component
public class FakeStoreProductService implements ProductService {
    
    private static final String URL = "https://fakestoreapi.com/products";
    
    private final RestTemplate restTemplate;
    
    public FakeStoreProductService(RestTemplateClient restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<Product> getAllProducts() {
        return restTemplate.getForObject(URL, List.class);
    }
}
```

Below are some properties by which you can update the default configuration value
```
skmonjurul.resttemplate.connection-pool.max-total-connections=50
skmonjurul.resttemplate.route.max-connections=50
skmonjurul.resttemplate.http-client.connection-timeout=20000
skmonjurul.resttemplate.keep-alive.default-time=30000
skmonjurul.resttemplate.request.timeout=20000
skmonjurul.resttemplate.socket.timeout=20000
```

## Health Information
You can use health information to check the status of your running application. It is often used by monitoring tools to 
check the status and alert. you can check the application health by going to the `/actuator/health` endpoint. By default,
`health` endpoint is enabled and exposed.

```
{
    "status": "UP"
}
```

The information exposed by the health endpoint depends on the `management.endpoint.health.show-details` property and 
`management.endpoint.health.show-components` property, which can be configured with one of the following values:


| Name            | Description                                                                                                                                 |
|-----------------|---------------------------------------------------------------------------------------------------------------------------------------------|
| never           | Details are never shown.                                                                                                                    |
| when_authorized | Details are only shown when the user is authorized. Authorized roles can be configured by using `management.endpoint.health.roles` property |
| always          | Details are always shown to all users.                                                                                                      |


The default value is `never`. A user is considered authorized if they are in one or more of the endpoint's roles
If the endpoint has no configured roles, all authenticated users are considered authorized.
You can configure the roles bt using the `management.endpoint.health.roles` property.

### Writing custom health indicator
To provide custom health information, you can register Spring beans that extend `ServiceHealthIndicator` abstract class
or implement `HealthIndicator` interface. If you extend `ServiceHealthIndicator` abstract class, you need to implement
`getServiceName()`, `getServiceBaseUrl()`, and `getHealthCheckEndpoint()` method. 

Or

If you implement `HealthIndicator` interface, you need to implement `health()` method.

| Method                   | Description                                                                                      |
|--------------------------|--------------------------------------------------------------------------------------------------|
| getServiceName()         | Provide the service name for which you are creating the health indicator                         |
| getServiceBaseUrl()      | Provide the base URL of the service for which you are creating the health indicator              |
| getHealthCheckEndpoint() | Provide the health check endpoint of the service for which you are creating the health indicator |


The following code snippet shows how to create a custom health indicator.

`HealthIndicator` interface

```java
package com.skmonjurul.product_service.health;

import org.springframework.stereotype.Component;

@Component
public class MyHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        int errorCode = check();
        if (errorCode != 0) {
            return Health.down().withDetail("Error Code", errorCode).build();
        }
        return Health.up().withDetail("My Custom Health Indicator", "UP").build();
    }
    
    private int check() {
        // perform some specific health check
        return 1;
    }
}
```

`ServiceHealthIndicator` abstract class

```java
package com.skmonjurul.product_service.health;

import org.springframework.stereotype.Component;

@Component
public class MyServiceHealthIndicator extends ServiceHealthIndicator {
    
    protected MyServiceHealthIndicator(RestTemplate restTemplate) {
        super(restTemplate);
    }
    
    @Override
    public String getServiceName() {
        return "MyServiceName";
    }
    
    @Override
    public String getServiceBaseUrl() {
        return "http://localhost:8080";
    }
    
    @Override
    public String getHealthCheckEndpoint() {
        return "q/health";
    }
}
```