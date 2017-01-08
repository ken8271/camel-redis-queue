package org.apache.camel.component.redis;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.redisson.api.RBlockingQueue;

import com.alibaba.fastjson.JSONObject;

/**
 * The camel producer.
 */
public class RedisQueueProducer extends DefaultProducer {
    
    private RBlockingQueue<String> queue;

    public RedisQueueProducer(RedisQueueEndpoint endpoint , RBlockingQueue<String> queue) {
        super(endpoint);
        this.queue = queue;
    }

    public void process(Exchange exchange) throws Exception {
    	if(exchange != null && exchange.getIn().getBody() != null)
    		this.queue.put(JSONObject.toJSONString(exchange.getIn().getBody()));
    	else
    		throw new IllegalArgumentException("the message body could not be null");
    }

}
