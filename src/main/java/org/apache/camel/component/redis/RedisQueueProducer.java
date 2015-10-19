package org.apache.camel.component.redis;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.springframework.data.redis.core.ListOperations;

import com.alibaba.fastjson.JSONObject;

/**
 * The camel producer.
 */
public class RedisQueueProducer extends DefaultProducer {
    
    private RedisQueueConfiguration configuration;
    
    private ListOperations<String, String> operation;

    public RedisQueueProducer(RedisQueueEndpoint endpoint , RedisQueueConfiguration configuration) {
        super(endpoint);
        this.configuration = configuration;
        this.operation = configuration.getRedisTemplate().opsForList();
    }

    public void process(Exchange exchange) throws Exception {
    	if(exchange != null && exchange.getIn().getBody() != null)
    		operation.leftPush(configuration.getChannel(), JSONObject.toJSONString(exchange.getIn().getBody()));
    	else
    		throw new IllegalArgumentException("the message body could not be null");
    }

}
