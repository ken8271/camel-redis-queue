package org.apache.camel.component.redis;

import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriParams;
import org.apache.camel.spi.UriPath;
import org.springframework.data.redis.core.RedisTemplate;

@UriParams
public class RedisQueueConfiguration {

	@UriPath  @Metadata(required = "true")
	private String channel;
	
	@UriParam
	private RedisTemplate<String, String> redisTemplate;

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public RedisTemplate<String, String> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

}
