package org.apache.camel.component.redis;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ListOperations;

/**
 * The camel consumer.
 */
public class RedisQueueConsumer extends DefaultConsumer {
	
	private static final Logger logger = LoggerFactory.getLogger(RedisQueueConsumer.class);
	
    private RedisQueueConfiguration configuration;
    
    private ListOperations<String, String> operation;
    
    private boolean runnable = true;
    
    private ExecutorService executor;

    public RedisQueueConsumer(RedisQueueEndpoint endpoint, Processor processor , RedisQueueConfiguration configuration) {
        super(endpoint, processor);
        this.configuration = configuration;
        this.operation = configuration.getRedisTemplate().opsForList();
        this.executor = Executors.newSingleThreadExecutor();
    }

    @Override
    protected void doStart() throws Exception {
    	super.doStart();
    	this.runnable = true;
    	executor.execute(new Runnable() {
			
			@Override
			public void run() {
				while(runnable){
					try {
						String value = operation.rightPop(configuration.getChannel());
						
						if(value != null && value.length() != 0)
							onMessage(value);
					} catch (Exception e) {
						logger.error("[run] - fail to loop the redis , the exception is " + e.getMessage() , e);
					}
				}
			}
		});
    }
    
    private void onMessage(String message) throws Exception{
    	getProcessor().process(buildExchange(message));
    }
    
    private Exchange buildExchange(String message){
    	Exchange exchange = getEndpoint().createExchange();
    	
    	exchange.getIn().setBody(message);
    	
    	return exchange;
    }
    
    @Override
    protected void doStop() throws Exception {
    	super.doStop();
    	this.runnable = false;
    }
    
    @Override
    protected void doShutdown() throws Exception {
    	super.doShutdown();
    	this.runnable = false;
    }
    
    
}
