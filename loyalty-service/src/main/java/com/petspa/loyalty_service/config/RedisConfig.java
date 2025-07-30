package com.petspa.loyalty_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName("redis-16950.c334.asia-southeast2-1.gce.redns.redis-cloud.com");
        config.setPort(16950);
        config.setUsername("default");
        config.setPassword(RedisPassword.of("AipQz8HQScYIIA2DW86LABGDQmLY5iVC"));

        // Không dùng SSL
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .build(); // Loại bỏ .useSsl()

        return new LettuceConnectionFactory(config, clientConfig);
    }

    @Bean
    public RedisTemplate<String, Integer> redisTemplate() {
        RedisTemplate<String, Integer> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        return template;
    }
}