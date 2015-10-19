package org.apache.camel.component.redis;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.UriEndpoint;

/**
 * Represents a camel endpoint.
 */
@UriEndpoint(scheme = "redis", syntax="redis:queue:channel", consumerClass = RedisQueueConsumer.class, label = "camel,redis,spring")
public class RedisQueueEndpoint extends DefaultEndpoint {
	
	private RedisQueueConfiguration configuration;
	
    public RedisQueueEndpoint(String uri, RedisQueueComponent component , RedisQueueConfiguration configuration) {
        super(uri, component);
        this.configuration = configuration;
    }

    public Producer createProducer() throws Exception {
        return new RedisQueueProducer(this , configuration);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        return new RedisQueueConsumer(this, processor , configuration);
    }

    public boolean isSingleton() {
        return true;
    }

}
