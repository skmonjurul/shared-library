package com.skmonjurul.shared_library.web.health;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public abstract class ServiceHealthIndicator extends AbstractHealthIndicator {
    private final RestTemplate restTemplate;
    private static final String UP = "UP";
    private static final String FRONT_SLASH = "/";
    
    protected ServiceHealthIndicator(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        builder.up().withDetail("name", getServiceName());
        boolean status = check();
        builder.status(status ? Status.UP : Status.DOWN);
    }
    
    @SuppressWarnings("unchecked")
    private boolean check() {
        Map<String, Object> response = restTemplate
                .getForObject(getServiceBaseUrl() + FRONT_SLASH + getHealthCheckEndpoint(), Map.class);
        assert response != null;
        String status = (String) response.get("status");
        return UP.equals(status);
    }
    
    /**
     * Implement this method to return the base URL of the service
     * @return the base URL of the service
     */
    protected abstract String getServiceBaseUrl();
    
    /**
     * Implement this method to return the name of the service
     * @return the name of the service
     */
    protected abstract String getServiceName();
    
    /**
     * Implement this method to return the health check endpoint of the service
     * @return the health check endpoint of the service
     */
    protected abstract String getHealthCheckEndpoint();
}
