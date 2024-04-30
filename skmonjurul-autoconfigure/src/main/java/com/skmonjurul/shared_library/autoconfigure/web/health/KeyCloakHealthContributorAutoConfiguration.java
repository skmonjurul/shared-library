package com.skmonjurul.shared_library.autoconfigure.web.health;

import com.skmonjurul.shared_library.autoconfigure.web.client.RestTemplateAutoConfiguration;
import com.skmonjurul.shared_library.web.health.KeycloakHealthIndicator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;


@AutoConfiguration(after = RestTemplateAutoConfiguration.class)
@ConditionalOnClass({ KeycloakHealthIndicator.class, JwtAuthenticationProvider.class })
@ConditionalOnProperty(name = "spring.security.oauth2.resourceserver.jwt.jwk-set-uri")
@ConditionalOnEnabledHealthIndicator("keycloak")
public class KeyCloakHealthContributorAutoConfiguration implements InitializingBean {
    private final RestTemplate restTemplate;
    private final OAuth2ResourceServerProperties properties;
    
    public KeyCloakHealthContributorAutoConfiguration(RestTemplate restTemplate, OAuth2ResourceServerProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }
    
    
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.hasText(properties.getJwt().getJwkSetUri(), "jwkSetUri must not be empty." +
                "Set the jwkSetUri in application.properties or application.yml to enable Keycloak health check.");
    }
    
    @Bean
    public HealthContributor keycloakHealthContributor() {
        return new KeycloakHealthIndicator(restTemplate, properties.getJwt().getJwkSetUri());
    }
}
