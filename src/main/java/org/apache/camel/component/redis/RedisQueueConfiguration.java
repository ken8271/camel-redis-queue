package org.apache.camel.component.redis;

import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriParams;
import org.apache.camel.spi.UriPath;
import org.redisson.api.RedissonClient;

@UriParams
public class RedisQueueConfiguration {

	@UriPath  @Metadata(required = "true")
	private String channel;
	
	@UriParam(defaultValue="2000")
	private int timeout;
	
	@UriParam
	private RedissonClient redisson;

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public RedissonClient getRedisson() {
		return redisson;
	}

	public void setRedisson(RedissonClient redisson) {
		this.redisson = redisson;
	}
	
}
