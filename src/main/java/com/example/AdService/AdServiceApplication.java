package com.example.AdService;

//import com.example.AdService.document.TrendingCache;
import com.example.AdService.document.UserCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;

@Async
@EnableFeignClients
@EnableCaching
@SpringBootApplication
@EnableKafka

public class AdServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdServiceApplication.class, args);

	}


	@Bean
	JedisConnectionFactory jedisConnectionFactory(){
		return new JedisConnectionFactory();
	}

	@Bean
	RedisTemplate<String, UserCache> redisTemplate(){
		RedisTemplate<String,UserCache> redisTemplate = new RedisTemplate<String, UserCache>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		return redisTemplate;
	}

//	@Bean
//	RedisTemplate<String, TrendingCache> redisTemplate1(){
//		RedisTemplate<String,TrendingCache> redisTemplate = new RedisTemplate<String, TrendingCache>();
//		redisTemplate.setConnectionFactory(jedisConnectionFactory());
//		return redisTemplate;
//	}


}
