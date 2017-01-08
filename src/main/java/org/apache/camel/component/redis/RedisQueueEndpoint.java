package org.apache.camel.component.redis;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.UriEndpoint;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;

/**
 * Represents a camel endpoint.
 */
@UriEndpoint(scheme = "redis", syntax="redis:queue:channel", consumerClass = RedisQueueConsumer.class, label = "camel,redis,spring")
public class RedisQueueEndpoint extends DefaultEndpoint {
	
	private RedissonClient redisson;
	
	private RBlockingQueue<String> queue;
	
    public RedisQueueEndpoint(String uri, RedisQueueComponent component , RedisQueueConfiguration configuration) {
        super(uri, component);
        this.redisson = configuration.getRedisson();
        this.queue = redisson.getBlockingQueue(configuration.getChannel(), new StringCodec("UTF-8"));
    }
    
    public Producer createProducer() throws Exception {
        return new RedisQueueProducer(this , queue);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        return new RedisQueueConsumer(this, processor, queue);
    }

    public boolean isSingleton() {
        return true;
    }

}
