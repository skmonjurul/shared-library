package com.skmonjurul.shared_library.autoconfigure.web.client;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "skmonjurul.resttemplate")
public class RestTemplateClientProperties {
    
    private final ConnectionPool connectionPool = new ConnectionPool();
    
    private final Route route = new Route();
    
    private final KeepAlive keepAlive = new KeepAlive();
    
    private final HttpClient httpClient = new HttpClient();
    
    private final Request request = new Request();
    
    private final Socket socket = new Socket();
    
    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }
    
    public Route getRoute() {
        return route;
    }
    
    public KeepAlive getKeepAlive() {
        return keepAlive;
    }
    
    public HttpClient getHttpClient() {
        return httpClient;
    }
    
    public Request getRequest() {
        return request;
    }
    
    public Socket getSocket() {
        return socket;
    }
    
    public static class ConnectionPool {
        
        /**
         * Max total connection is the connection pool across all HTTP routes.
         * Default value is 40.
         *
         */
        private int maxTotalConnections = 40;
        
        public int getMaxTotalConnections() {
            return maxTotalConnections;
        }
        
        public void setMaxTotalConnections(int maxTotalConnections) {
            this.maxTotalConnections = maxTotalConnections;
        }
    }
    
    public static class Route {
        
        /**
         * Max connections per route or host.
         * Default value is 40.
         */
        private int maxConnections = 40;
        
        public int getMaxConnections() {
            return maxConnections;
        }
        
        public void setMaxConnections(int maxConnections) {
            this.maxConnections = maxConnections;
        }
    }
    
    public static class KeepAlive {
        
        /**
         * The connections are kept alive.
         * Default is 20 seconds.
         */
        private int defaultTime = 20000; // 20 seconds
        
        public int getDefaultTime() {
            return defaultTime;
        }
        
        public void setDefaultTime(int defaultTime) {
            this.defaultTime = defaultTime;
        }
    }
    
    public static class HttpClient {
        
        /**
         * Max time that is waited for a connection to be established.
         * Default value is 30 sec.
         */
        private int connectionTimeout = 30000; // 30 sec, the time for waiting until a connection is established
        
        public int getConnectionTimeout() {
            return connectionTimeout;
        }
        
        public void setConnectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
        }
    }
    
    public static class Request {
        /**
         * Max time that is waited until a connection from connection pool is available.
         * Default value is 30 sec.
         */
        private int timeout = 30000; // 30 sec. The time for waiting for a connection from connection pool
        
        public int getTimeout() {
            return timeout;
        }
        
        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }
    }
    
    public static class Socket {
        /**
         * Max time that is waited until data is received when a connection is established.
         * Default value is 60 sec.
         */
        private int timeout = 60000; // 60 sec. The time for waiting for data.
        
        public int getTimeout() {
            return timeout;
        }
        
        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }
    }
}
