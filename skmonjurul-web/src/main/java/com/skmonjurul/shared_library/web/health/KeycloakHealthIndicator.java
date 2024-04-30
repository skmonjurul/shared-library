package com.skmonjurul.shared_library.web.health;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Map;

public class KeycloakHealthIndicator extends AbstractHealthIndicator {
    private final RestTemplate restTemplate;
    private final String keycloakUrl;
    private static final String UP = "UP";
    private static final String FRONT_SLASH = "/";
    private static final String KEYCLOAK_HEALTH_CHECK_ENDPOINT = "health/live";
    
    public KeycloakHealthIndicator(RestTemplate restTemplate, String keycloakUrl) {
        this.restTemplate = restTemplate;
        this.keycloakUrl = keycloakUrl;
    }
    
    
    @SuppressWarnings("unchecked")
    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        Map<String, Object> response = restTemplate
                .getForObject(getBaseUrl(keycloakUrl) + FRONT_SLASH + KEYCLOAK_HEALTH_CHECK_ENDPOINT, Map.class);
        assert response != null;
        String status = (String) response.get("status");
        builder.status(UP.equals(status) ? Status.UP : Status.DOWN);
    }
    
    private String getBaseUrl(String keycloakUrl) throws MalformedURLException {
        URL url = URI.create(keycloakUrl).toURL();
        return url.getProtocol() + "://" + url.getAuthority();
    }
}
