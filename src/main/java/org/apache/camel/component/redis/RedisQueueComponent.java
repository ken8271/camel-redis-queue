package org.apache.camel.component.redis;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.UriEndpointComponent;

/**
 * Represents the component that manages {@link RedisQueueEndpoint}.
 */
public class RedisQueueComponent extends UriEndpointComponent {
    
    public RedisQueueComponent() {
        super(RedisQueueEndpoint.class);
    }

    public RedisQueueComponent(CamelContext context) {
        super(context, RedisQueueEndpoint.class);
    }

    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        RedisQueueConfiguration configuration = new RedisQueueConfiguration();
        
        setChannel(configuration, remaining);
        setProperties(configuration, parameters);
        
        return new RedisQueueEndpoint(uri, this , configuration);
    }
    
    private void setChannel(RedisQueueConfiguration configuration , String remaining){
    	String[] paths = remaining.split(":");

        if (paths.length > 1 && paths[1].length() > 0) {
           configuration.setChannel(paths[1]);
        }
    }
}
