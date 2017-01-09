package org.apache.camel.component.redis;

import java.util.concurrent.TimeUnit;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.redisson.api.RBlockingQueue;

import com.alibaba.fastjson.JSONObject;

/**
 * The camel producer.
 */
public class RedisQueueProducer extends DefaultProducer {
    
    private RBlockingQueue<String> queue;
    
    private int timeout;

    public RedisQueueProducer(RedisQueueEndpoint endpoint , RBlockingQueue<String> queue , int timeout) {
        super(endpoint);
        this.queue = queue;
        this.timeout = timeout;
    }

    public void process(Exchange exchange) throws Exception {
    	if(exchange != null && exchange.getIn().getBody() != null)
    		this.queue.offer(JSONObject.toJSONString(exchange.getIn().getBody()), timeout, TimeUnit.MICROSECONDS);
    	else
    		throw new IllegalArgumentException("the message body could not be null");
    }

}
