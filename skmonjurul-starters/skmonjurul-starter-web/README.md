# Custom Spring Boot Starter for service to service communication

## How to use
### Add this starter as a dependency in your pom.xml file of your spring boot client application

```
<dependency>
    <groupId>com.skmonjurul.shared-library</groupId>
    <artifactId>skmonjurul-starter-web</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### Inject `RestTemplateClient` as a bean
```
package com.skmonjurul.product_service.service;

import com.skmonjurul.product_service.exception.ResourceNotFoundException;
import com.skmonjurul.product_service.openapi.model.Product;
import com.skmonjurul.shared_library.web.client.RestTemplateClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;


@Service
public class FakeStoreProductService implements ProductService {
    
    private static final String URL = "https://fakestoreapi.com/products";
    
    private final RestTemplateClient restTemplate;
    
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

### Below are the properties by which you can update the default value
```
skmonjurul.resttemplate.connection-pool.max-total-connections=50
skmonjurul.resttemplate.route.max-connections=50
skmonjurul.resttemplate.http-client.connection-timeout=20000
skmonjurul.resttemplate.keep-alive.default-time=30000
skmonjurul.resttemplate.request.timeout=20000
skmonjurul.resttemplate.socket.timeout=20000
```