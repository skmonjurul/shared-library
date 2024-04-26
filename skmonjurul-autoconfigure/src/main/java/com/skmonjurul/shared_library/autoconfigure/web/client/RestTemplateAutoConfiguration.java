package com.skmonjurul.shared_library.autoconfigure.web.client;


import static com.skmonjurul.shared_library.web.client.HttpClientConfigConstants.*;

import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HeaderElement;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.http.message.BasicHeaderElementIterator;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;

import java.util.Iterator;

@AutoConfiguration(after = HttpMessageConvertersAutoConfiguration.class, before = RestTemplateBuilder.class)
@ConditionalOnClass(RestTemplate.class)
@Conditional(NotReactiveWebApplicationCondition.class)
@EnableConfigurationProperties(RestTemplateClientProperties.class)
public class RestTemplateAutoConfiguration {
    
    private final RestTemplateClientProperties properties;
    
    public RestTemplateAutoConfiguration(RestTemplateClientProperties properties) {
        this.properties = properties;
    }
    
    
    @Bean
    @Lazy
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        return new RestTemplate(clientHttpRequestFactory());
    }
    
    
    /**
     * The HttpComponentsClientHttpRequestFactory class is an implementation of the ClientHttpRequestFactory interface.
     * It is used to create a new instance of the HttpComponentsClientHttpRequest class.
     * @return {@link ClientHttpRequestFactory}
     * See also {@link HttpComponentsClientHttpRequestFactory}
     */
    @Bean
    @Lazy
    @ConditionalOnMissingBean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClient());
        clientHttpRequestFactory.setConnectionRequestTimeout(properties.getRequest().getTimeout());
        clientHttpRequestFactory.setConnectTimeout(properties.getHttpClient().getConnectionTimeout());
        
        return clientHttpRequestFactory;
    }
    
    
    /**
     * The CloseableHttpClient interface represents a client that can execute HTTP requests.
     * It is a higher-level abstraction than HttpClient. It is a client that can execute requests and return responses.
     * @return {@link CloseableHttpClient}
     */
    @Bean
    @Lazy
    @ConditionalOnMissingBean
    public CloseableHttpClient httpClient() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(properties.getRequest().getTimeout()))
                .build();
        
        return HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(poolingHttpClientConnectionManager())
                .setKeepAliveStrategy(connectionKeepAliveStrategy())
                .build();
    }
    
    
    /**
     * The ConnectionKeepAliveStrategy interface allows you to define how long a connection should be kept alive.
     * This is useful when you have a connection pool, you want to reuse connections.
     * The default time is 60 seconds.
     * @return {@link ConnectionKeepAliveStrategy}
     */
    @Bean
    @Lazy
    @ConditionalOnMissingBean
    public ConnectionKeepAliveStrategy connectionKeepAliveStrategy() {
        return ((httpResponse, httpContext) -> {
            Iterator<Header> headerIterator = httpResponse.headerIterator(CONN_KEEP_ALIVE);
            BasicHeaderElementIterator elementIterator = new BasicHeaderElementIterator(headerIterator);
            
            while (elementIterator.hasNext()) {
                HeaderElement element = elementIterator.next();
                String param = element.getName();
                String value = element.getValue();
                if (value != null && param.equalsIgnoreCase(TIMEOUT)) {
                    return TimeValue.ofMilliseconds(Long.parseLong(value) * 1000); // convert to ms
                }
            }
            
            return TimeValue.ofMilliseconds(properties.getKeepAlive().getDefaultTime());
        });
    }
    
    
    /**
     * A connection pool is a collection of connections that can be reused.
     * It ensures that connections are reused rather than created each time.
     * This means that connections don't have to be re-established each time, saving us a lot of overhead and time.
     * Especially the handshake process when establishing a connection can be very time-consuming.
     * The number of connections in the pool can be defined in total and per route.
     * @return {@link PoolingHttpClientConnectionManager}
     */
    @Bean
    @Lazy
    @ConditionalOnMissingBean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(properties.getConnectionPool().getMaxTotalConnections());
        connectionManager.setDefaultMaxPerRoute(properties.getRoute().getMaxConnections());
        connectionManager.setDefaultSocketConfig(socketConfig());
        connectionManager.setDefaultConnectionConfig(connectionConfig());
        
        return connectionManager;
    }
    
    private ConnectionConfig connectionConfig() {
        return ConnectionConfig.custom()
                .setConnectTimeout(Timeout.ofMilliseconds(properties.getHttpClient().getConnectionTimeout()))
                .build();
    }
    
    private SocketConfig socketConfig() {
        return SocketConfig.custom()
                .setSoTimeout(Timeout.ofMilliseconds(properties.getSocket().getTimeout()))
                .build();
    }
}
