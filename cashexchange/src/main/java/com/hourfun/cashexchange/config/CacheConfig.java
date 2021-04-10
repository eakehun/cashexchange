package com.hourfun.cashexchange.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

@Configuration
public class CacheConfig extends CachingConfigurerSupport {

	@Autowired
	private RedisConnectionFactory redisConnectionFactory;

//	@Bean
//	public EhCacheManagerFactoryBean getEhCacheFactory() {
//		EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
//		factoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
//		factoryBean.setShared(true);
//		
//		return factoryBean;
//	}

	@Bean
	@Override
	public CacheManager cacheManager() {
		
		ObjectMapper mapper = new ObjectMapper();
	    mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
	    mapper.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

	    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

	    // Registering Hibernate5Module to support lazy objects for hibernate 5
	    // Use Hibernate4Module if using hibernate 4 
	    mapper.registerModule(new Hibernate5Module());

		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
				.disableCachingNullValues().entryTtl(Duration.ofMinutes(1))
				.serializeKeysWith(
						RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
				.serializeValuesWith(RedisSerializationContext.SerializationPair
						.fromSerializer(new GenericJackson2JsonRedisSerializer()));

		redisCacheConfiguration.usePrefix();

		return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory)
				.cacheDefaults(redisCacheConfiguration).build();

	}

}
