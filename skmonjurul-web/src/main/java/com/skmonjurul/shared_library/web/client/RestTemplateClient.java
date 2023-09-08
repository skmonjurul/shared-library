package com.skmonjurul.shared_library.web.client;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class RestTemplateClient extends RestTemplate implements HttpClient{
    public RestTemplateClient() {
        super();
    }
    
    public RestTemplateClient(ClientHttpRequestFactory requestFactory) {
        super(requestFactory);
    }
    
    public RestTemplateClient(List<HttpMessageConverter<?>> messageConverters) {
        super(messageConverters);
    }
}
