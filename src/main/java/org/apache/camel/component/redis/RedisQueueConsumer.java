package org.apache.camel.component.redis;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The camel consumer.
 */
public class RedisQueueConsumer extends DefaultConsumer implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(RedisQueueConsumer.class);
	
	private static final int DEFAULT_WORK_THREADS = 1;
	
	private RBlockingQueue<String> queue;
	
	private ExecutorService executor;
	
    public RedisQueueConsumer(RedisQueueEndpoint endpoint , Processor processor, RBlockingQueue<String> queue) {
    	super(endpoint, processor);
        this.queue = queue;
        this.executor = Executors.newFixedThreadPool(DEFAULT_WORK_THREADS);
        this.executor.submit(this);
    }
    
    @Override
    public void run() {
    	try {
			doPull();
		} catch (Exception e) {
			logger.error("fail to pull exchange message." , e);
			getExceptionHandler().handleException(e);
		}
    }
    
    private void doPull() throws Exception{
    	RFuture<String> future = this.queue.takeAsync();
    	
    	future.addListener(new FutureListener<String>() {
    		@Override
    		public void operationComplete(Future<String> future)
    				throws Exception {
    			if(!future.isSuccess()){
					logger.error("[doPull] - fail to pull message from redis." , future.cause());
					doPull();
					return ;
				}
				
    			onMessage(future.getNow());
    			
    			doPull();
    		}
		});
    }
    
    private void onMessage(String message){
    	try {
    		if(message != null)
    			getProcessor().process(buildExchange(message));
		} catch (Exception e) {
			logger.error("[onMessage] - fail to process message." , e);
			getExceptionHandler().handleException(e);
		}
    }
    
    private Exchange buildExchange(String message){
    	Exchange exchange = getEndpoint().createExchange();
    	
    	exchange.getIn().setBody(message);
    	
    	return exchange;
    }
}
